package com.example.application.views.wh;

import com.example.application.components.FormControls;
import com.example.application.components.WeeklySummary;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
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

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIcon;

@CssImport("./themes/intern-project/views/workHours.css")
@PageTitle("Empty")
@Route(value = ":id", layout = MainLayout.class)
@RouteAlias(value = "")
public class WH extends Div {
    private final WorkLogService service;
    private final Button drawerToggleButton;
    private final BeanValidationBinder<WorkLog> binder;
    private final Grid<WorkLog> grid;
    private final FormControls formControls;
    private final WeeklySummary weeklySummary;

    private WorkLog currentWorkLog;

    public WH(WorkLogService service) {
        addClassName("workhours-view");

        this.service = service;

        binder = new BeanValidationBinder<>(WorkLog.class);
        formControls = new FormControls(binder, service);
        weeklySummary = new WeeklySummary("Joseph", service);
        currentWorkLog = new WorkLog();

        Div entryEditPanel = new Div();

        grid = new Grid<>(WorkLog.class, false);
        grid.addColumn(WorkLog::getStartDate).setHeader("Date")
                .setSortable(true);
        grid.addColumn(WorkLog::getMinutes).setHeader("Minutes")
                .setSortable(true);
        grid.addColumn(WorkLog::getProjectName).setHeader("Project")
                .setSortable(true);
        grid.addColumn(WorkLog::getEmployeeName).setHeader("Employee")
                .setSortable(true);
        grid.addColumn(WorkLog::getDescription).setHeader("Description")
                .setSortable(true);

        List<WorkLog> workLogs = service.getAllTimes();
        grid.setItems(workLogs);

        grid.asSingleSelect().addValueChangeListener(event -> {
            boolean noSelection = (event.getValue() != null);
            if (noSelection) {
                populateForm(event.getValue());
                currentWorkLog = event.getValue();
                formControls.deleteButton
                        .removeClassName(LumoUtility.Display.HIDDEN);
            } else {
                resetForm();
                formControls.deleteButton
                        .addClassName(LumoUtility.Display.HIDDEN);
            }
            formControls.saveButton.setText(noSelection ? "UPDATE" : "SAVE");
            formControls.resetButton.setText(noSelection ? "RESET" : "CANCEL");
        });

        drawerToggleButton = new Button(
                LineAwesomeIcon.ANGLE_UP_SOLID.create());
        drawerToggleButton.addClassName("weekly-summary-button");
        drawerToggleButton.addClickListener(clickEvent -> {
            if (drawerToggleButton.hasClassName("weekly-summary-closed"))
                drawerToggleButton.removeClassName("weekly-summary-closed");
            else
                drawerToggleButton.addClassName("weekly-summary-closed");

            weeklySummary.toggleSummary();
        });

        formControls.deleteButton.addClickListener(e -> {
            if (this.currentWorkLog != null) {
                service.deleteOne(this.currentWorkLog);
                workLogs.removeIf(
                        obj -> obj.getId() == this.currentWorkLog.getId());
                grid.setItems(workLogs);
            } else {
                System.out.println("Someone did something illegal");
            }
            grid.select(null);

            resetForm();
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
                    currentWorkLog = new WorkLog();
                }
                binder.writeBean(this.currentWorkLog);
                service.saveWorkLog(currentWorkLog);
                Notification.show("Data updated");

                // Adding the new entry directly removes the need for a page
                // reload.
                if (newEntry) {
                    workLogs.add(this.currentWorkLog);
                    grid.setItems(workLogs);
                }

                resetForm();
                refreshGrid();

            } catch (ObjectOptimisticLockingFailureException exception) {
                // System.out.println(exception);
            } catch (ValidationException validationException) {
                // System.out.println(validationException);
            }
        });

        add(weeklySummary, drawerToggleButton, formControls, entryEditPanel,
                grid);

        setSizeFull();
        getStyle().set("text-align", "center");
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void resetForm() {
        if (this.currentWorkLog == null)
            return;

        weeklySummary.updateWeekDaySummary(currentWorkLog.getStartDate(),
                WeeklySummary.reduceMinutesListToString(service
                        .getTimesForDay(currentWorkLog.getStartDate(), 2)));
        populateForm(null);
    }

    private void populateForm(WorkLog entry) {
        this.currentWorkLog = entry;
        binder.readBean(this.currentWorkLog);

        if (this.currentWorkLog == null)
            formControls.setDefaults();
    }
}
