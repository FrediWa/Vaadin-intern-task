package com.example.application.views.wh;

import com.example.application.components.FormControls;
import com.example.application.components.WeeklySummary;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Svg;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;
import com.example.application.views.MainLayout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIcon;

@CssImport("./themes/intern-project/views/workHours.css")
@PageTitle("Empty")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "")
public class WH extends Div {
    WorkLogService service;
    Button drawerToggleButton;
    private final BeanValidationBinder<WorkLog> binder;
    private WorkLog currentWorkLog;
    Grid<WorkLog> grid;
    FormControls formControls;
    WeeklySummary weeklySummary;

    public WH(WorkLogService service) {
        this.service = service;
        binder = new BeanValidationBinder<>(WorkLog.class);
        formControls = new FormControls(binder, service);
        weeklySummary = new WeeklySummary("Joseph", service);

        Div entryEditPanel = new Div();

        grid = new Grid<>(WorkLog.class, false);
        grid.addColumn(WorkLog::getStartDate).setHeader("Date").setSortable(true);
        grid.addColumn(WorkLog::getMinutes).setHeader("Minutes").setSortable(true);
        grid.addColumn(WorkLog::getProjectName).setHeader("Project").setSortable(true);
        grid.addColumn(WorkLog::getEmployeeName).setHeader("Employee").setSortable(true);
        grid.addColumn(WorkLog::getDescription).setHeader("Description").setSortable(true);

        List<WorkLog> workLogs = service.getAllTimes();
        grid.setItems(workLogs);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue());
                this.currentWorkLog = event.getValue();
                formControls.saveButton.setText("Update");
                formControls.resetButton.setText("Cancel");

                formControls.deleteButton.removeClassName(LumoUtility.Display.HIDDEN);
            }
            else {
                resetForm();
                formControls.saveButton.setText("Add");
                formControls.resetButton.setText("Reset");
                formControls.deleteButton.addClassName(LumoUtility.Display.HIDDEN);
            }
        });
        
        drawerToggleButton = new Button(LineAwesomeIcon.ANGLE_UP_SOLID.create());
        drawerToggleButton.addClassName("weekly-summary-button");
        drawerToggleButton.addClickListener(clickEvent -> {
            if (drawerToggleButton.hasClassName("weekly-summary-closed"))
                drawerToggleButton.removeClassName("weekly-summary-closed");
            else
                drawerToggleButton.addClassName("weekly-summary-closed");
            weeklySummary.toggleClass(weeklySummary, "weekly-summary-panel-closed");
        });

        formControls.deleteButton.addClickListener(e -> {
            if (this.currentWorkLog != null) {
                service.deleteOne(this.currentWorkLog);
                workLogs.removeIf(obj -> obj.getId() == this.currentWorkLog.getId());
                grid.setItems(workLogs);
            }else {
                System.out.println("Someone did something illegal");
            }
            grid.select(null);
            resetForm();
            grid.select(null);
            refreshGrid();
        });

        formControls.resetButton.addClickListener(e -> {
            resetForm();
            grid.select(null);
        });
        
        formControls.saveButton.addClickListener(e -> {
            try {
                boolean newEntry = currentWorkLog == null;
                if (newEntry) {
                    this.currentWorkLog = new WorkLog();
                }
                binder.writeBean(this.currentWorkLog);
                service.saveWorkLog(currentWorkLog);
                Notification.show("Data updated");
                
                // Adding the new entry directly removes the need for a page reload.
                if (newEntry) {
                    workLogs.add(this.currentWorkLog);
                    grid.setItems(workLogs);
                }

                resetForm();
                refreshGrid();

            } catch (ObjectOptimisticLockingFailureException exception) {
                System.out.println(exception);
            } catch (ValidationException validationException) {
                System.out.println(validationException);
            }
        });
    
        add(weeklySummary,drawerToggleButton, formControls, entryEditPanel, grid);

        setSizeFull();
        getStyle().set("text-align", "center");
    }
    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void resetForm() {
        System.out.println("Reset form");
        populateForm(null);
        weeklySummary.reloadWeekly(this.service, "Jo");
    }
  
    private void populateForm(WorkLog entry) {
        this.currentWorkLog = entry;
        binder.readBean(this.currentWorkLog);
        if (this.currentWorkLog == null) 
            formControls.setDefaults();
    }

}
