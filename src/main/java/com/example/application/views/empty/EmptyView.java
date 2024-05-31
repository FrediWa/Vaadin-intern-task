package com.example.application.views.empty;

import com.example.application.components.FormControls;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;

import java.time.LocalTime;
import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Empty")
@Route(value = "/:employeeId?/:action?(edit)")
@RouteAlias(value = "")
public class EmptyView extends VerticalLayout {
    WorkLogService service;
    private final BeanValidationBinder<WorkLog> binder;
    private WorkLog currentWorkLog;
    Grid<WorkLog> grid;
    FormControls formControls;

    public EmptyView(WorkLogService service) {
        this.service = service;
        binder = new BeanValidationBinder<>(WorkLog.class);
        formControls = new FormControls(binder, service);

        VerticalLayout entryEditPanel = new VerticalLayout();

        HorizontalLayout upperEditFields = new HorizontalLayout();

        upperEditFields.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        upperEditFields.setWidth("100%");
        
        grid = new Grid<>(WorkLog.class, false);
        grid.addColumn(WorkLog::getStartDate).setHeader("Date");
        grid.addColumn(WorkLog::getMinutes).setHeader("Minutes");
        grid.addColumn(WorkLog::getProjectName).setHeader("Project");
        grid.addColumn(WorkLog::getEmployeeName).setHeader("Employee");
        grid.addColumn(WorkLog::getDescription).setHeader("Description");

        List<WorkLog> workLogs = service.getAllTimes();
        grid.setItems(workLogs);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue());
                this.currentWorkLog = event.getValue();
                formControls.saveButton.setText("Update");
                formControls.resetButton.setText("Cancel");
            }
            else {
                resetForm();
                formControls.saveButton.setText("Add");
                formControls.resetButton.setText("Reset");
            }
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
                System.out.println("Should've updated now");
                
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

        entryEditPanel.add(formControls);
        add(entryEditPanel, grid);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void resetForm() {
        populateForm(null);
    }

    private void populateForm(WorkLog entry) {
        this.currentWorkLog = entry;
        binder.readBean(this.currentWorkLog);
        if (this.currentWorkLog == null) 
            formControls.setDefaults();
    }
}
