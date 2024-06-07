package com.example.application.views.workhours;

import com.example.application.components.FormControls;
import com.example.application.components.WeeklySummary;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingException;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;

import jakarta.annotation.security.PermitAll;

import com.example.application.data.models.Employee;
import com.example.application.data.models.MyUserDetails;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.SecurityService;
import com.example.application.data.services.WorkLogService;
import com.example.application.views.MainLayout;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIcon;

@CssImport("./themes/intern-project/views/workHours.css")
@PageTitle("Empty")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "")
@PermitAll
public class WorkHoursView extends Div {
    private final WorkLogService service;
    private final Button drawerToggleButton;
    private final BeanValidationBinder<WorkLog> binder;
    private final Grid<WorkLog> grid;
    private final FormControls formControls;
    private final WeeklySummary weeklySummary;
    private final DataProvider<WorkLog, Void> workLogDataProvider;
    private WorkLog currentWorkLog;

    public WorkHoursView(WorkLogService service, SecurityService securityService) {
        addClassName("workhours-view");

        this.service = service;
        MyUserDetails userDetails = securityService.getAuthenticatedUser();
        Employee userEmployee = service.getEmployee(userDetails.getEmployeeId());

        binder = new BeanValidationBinder<>(WorkLog.class);
        formControls = new FormControls(binder, service::getAllProjects,
                service::getAllEmployees);
        weeklySummary = new WeeklySummary(userEmployee, service::getEmployee,
                service::getTimesForDay);
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

        workLogDataProvider = DataProvider.fromCallbacks(query -> {
            int offset = query.getOffset();
            int limit = query.getLimit();
            return service.getAllTimes().stream().skip(offset).limit(limit);
        }, query -> service.getAllTimes().size());
        grid.setItems(workLogDataProvider);

        grid.asSingleSelect().addValueChangeListener(event -> {
            boolean noSelection = (event.getValue() != null);
            if (noSelection) {
                populateForm(event.getValue());
                currentWorkLog = event.getValue();
            } else {
                resetForm();
            }
            
            setFormButtons(noSelection);
            updateSummaries();
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
                workLogDataProvider.refreshAll();
            } else {
                System.out.println("Someone did something illegal");
            }
            updateSummaries();
            resetForm();
        });

        formControls.resetButton.addClickListener(e -> {
            resetForm();
            updateSummaries();
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
                Notification.show(newEntry ? "Created" : "Updated")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                workLogDataProvider.refreshAll();

                // resetForm();
            } catch (ObjectOptimisticLockingFailureException
                    | ValidationException | BindingException exception) {
                Notification.show("Missing data in fields")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                System.out.println(exception);
            }
            updateSummaries();
        });

        add(weeklySummary, drawerToggleButton, formControls, entryEditPanel,
                grid);

        setSizeFull();
        getStyle().set("text-align", "center");
    }

    private void setFormButtons(boolean entrySelected) {
        if (entrySelected) {
            formControls.deleteButton.removeClassName(LumoUtility.Display.HIDDEN);
        } else {
            formControls.deleteButton.addClassName(LumoUtility.Display.HIDDEN);
        }
            
        formControls.saveButton.setText(entrySelected ? "UPDATE" : "SAVE");
        formControls.resetButton.setText(entrySelected ? "CANCEL" : "RESET");
    }

    private void updateSummaries() {
        if (currentWorkLog != null) {
            weeklySummary.updateWeekDaySummary(currentWorkLog.getStartDate(),
            WeeklySummary.localTimeToString(WeeklySummary.reduceMinutesList(service
                    .getTimesForDay(currentWorkLog.getStartDate(), 2))));
            formControls.setSummaries(currentWorkLog, service::getTimesForDay);
        } else {
            formControls.setSummaries(null, service::getTimesForDay);
        }

        

    }
    private void resetForm() {
        if (this.currentWorkLog == null)
            return;

        setFormButtons(false);
        populateForm(null);
    }

    private void populateForm(WorkLog entry) {
        
        this.currentWorkLog = entry;
        binder.readBean(this.currentWorkLog);

        if (this.currentWorkLog == null)
            formControls.setDefaults();
    }
}
