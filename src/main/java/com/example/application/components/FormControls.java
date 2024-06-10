package com.example.application.components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.theme.lumo.LumoUtility;

@CssImport("./themes/intern-project/components/editPanel.css")
public class FormControls extends VerticalLayout {
    private final ComboBox<Project> projectsDropdown;
    private final ComboBox<Employee> employeeDropdown;
    private final DatePicker datePicker;
    private final TimePicker startTimePicker;
    private final HorizontalLayout upperFields;
    private final FormLayout lowerEditFields;
    private final FormLayout dailySummary;
    private final TimePicker dailyDeparture;
    private final TextField dailyTotal;
    private final TextField description;
    private final IntegerField minutesField;
    private final IntegerField absentField;
    private final HorizontalLayout entryEditPanelFooter;
    public final Button saveButton;
    public final Button resetButton;
    public final Button deleteButton;

    public FormControls(BeanValidationBinder<WorkLog> binder,
            Supplier<List<Project>> getAllProjects,
            Supplier<List<Employee>> getAllEmployees,
            Employee currentEmployee) {
        employeeDropdown = new ComboBox<>();
        datePicker = new DatePicker();
        startTimePicker = new TimePicker();
        absentField = new IntegerField();
        projectsDropdown = new ComboBox<>("Projects");
        description = new TextField("Description");
        minutesField = new IntegerField("Minutes");
        entryEditPanelFooter = new HorizontalLayout();
        upperFields = new HorizontalLayout();
        dailySummary = new FormLayout();
        lowerEditFields = new FormLayout();
        dailyDeparture = new TimePicker();
        dailyTotal = new TextField();

        setId("workhours-edit-panel");

        createLowerEditFields(getAllProjects.get());
        createUpperEditFields(getAllEmployees.get());

        saveButton = new Button("Add");
        resetButton = new Button("Reset");
        deleteButton = new Button("Delete");

        saveButton.setId("vaadin-add-button");

        deleteButton.addClassName(LumoUtility.Display.HIDDEN);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        entryEditPanelFooter.add(deleteButton, resetButton, saveButton);

        entryEditPanelFooter
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        entryEditPanelFooter.setWidth("100%");

        setDefaults(currentEmployee);

        bindFields(binder);
        H3 fromControlTitle = new H3("Add entry");
        add(fromControlTitle, upperFields, lowerEditFields,
                entryEditPanelFooter);

    }

    private void createUpperEditFields(List<Employee> employeeList) {
        FormLayout upperEditFields = new FormLayout();
        upperEditFields.setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("300px", 2), new ResponsiveStep("500px", 4));

        employeeDropdown.setPrefixComponent(new Icon("vaadin", "user"));
        employeeDropdown.setLabel("Employee");
        employeeDropdown.setItems(employeeList);
        employeeDropdown.setRequired(true);
        employeeDropdown.setRequiredIndicatorVisible(false);

        datePicker.setRequired(true);
        datePicker.setRequiredIndicatorVisible(false);
        datePicker.setLabel("Date");

        startTimePicker.setRequired(true);
        startTimePicker.setRequiredIndicatorVisible(false);
        startTimePicker.setLabel("Arrival");

        absentField.setStep(15);
        absentField.setStepButtonsVisible(true);
        absentField.setRequired(true);
        absentField.setRequiredIndicatorVisible(false);
        absentField.setLabel("Absent");

        dailyDeparture.setReadOnly(true);
        dailyTotal.setReadOnly(true);
        dailyDeparture.setLabel("Departure");
        dailyTotal.setLabel("Total");
        dailySummary.setResponsiveSteps(new ResponsiveStep("0", 2),
                new ResponsiveStep("300px", 2), new ResponsiveStep("500px", 2));
        dailySummary.add(dailyDeparture, dailyTotal);
        dailySummary.addClassName("form-control-daily-summary");

        upperEditFields.add(employeeDropdown, datePicker, startTimePicker,
                absentField);
        upperFields.add(upperEditFields, dailySummary);
        upperFields.setWidth("100%");
        upperFields.setJustifyContentMode(
                FlexComponent.JustifyContentMode.BETWEEN);
        upperFields.addClassName("form-control-upper-fields");

    }

    private void createLowerEditFields(List<Project> projectList) {

        minutesField.setStep(15);
        minutesField.setStepButtonsVisible(true);
        projectsDropdown.setItems(projectList);

        lowerEditFields.setColspan(description, 3);
        lowerEditFields.setResponsiveSteps(new ResponsiveStep("0", 3),
                new ResponsiveStep("300px", 5));

        lowerEditFields.add(minutesField, description, projectsDropdown);
        lowerEditFields.addClassName("form-control-lower-fields");
    }

    private void bindFields(BeanValidationBinder<WorkLog> binder) {
        binder.bind(projectsDropdown, WorkLog::getProject, WorkLog::setProject);
        binder.bind(description, WorkLog::getDescription,
                WorkLog::setDescription);
        binder.bind(minutesField, WorkLog::getMinutes, WorkLog::setMinutes);
        binder.bind(employeeDropdown, WorkLog::getEmployee,
                WorkLog::setEmployee);
        binder.bind(datePicker, WorkLog::getStartDate, WorkLog::setStartDate);
        binder.bind(startTimePicker, WorkLog::getStartTime,
                WorkLog::setStartTime);
        binder.bind(absentField, WorkLog::getAbsent, WorkLog::setAbsent);
    }

    public void setDefaults(Employee currentEmployee) {
        absentField.setValue(30);
        datePicker.setValue(LocalDate.now());
        employeeDropdown.setValue(currentEmployee);
    }

    public void setSummaries(WorkLog workLog,
            BiFunction<LocalDate, Long, List<Integer>> getTimesForDay) {
        if (workLog == null) {
            dailyDeparture.setValue(null);
            dailyTotal.setValue("");
            return;
        }

        LocalTime total = WeeklySummary.reduceMinutesList(getTimesForDay
                .apply(workLog.getStartDate(), workLog.getEmployee().getId()));

        LocalTime totalWithAbsent = total.plusMinutes(workLog.getAbsent());
        int totalMinutes = totalWithAbsent.getMinute();
        int totalHours = totalWithAbsent.getHour();
        LocalTime departure = workLog.getStartTime().plusHours(totalHours)
                .plusMinutes(totalMinutes);
        dailyDeparture.setValue(departure);
        dailyTotal
                .setValue(String.format("%sh%smin", totalHours, totalMinutes));
    }

}
