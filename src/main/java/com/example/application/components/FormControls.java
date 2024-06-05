package com.example.application.components;

import java.util.List;
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
    private final FormLayout upperEditFields;
    private final FormLayout lowerEditFields;
    private final TextField description;
    private final IntegerField minutesField;
    private final IntegerField absentField;
    private final HorizontalLayout entryEditPanelFooter;
    public final Button saveButton;
    public final Button resetButton;
    public final Button deleteButton;

    public FormControls(BeanValidationBinder<WorkLog> binder,
            Supplier<List<Project>> getAllProjects,
            Supplier<List<Employee>> getAllEmployees) {
        employeeDropdown = new ComboBox<>();
        datePicker = new DatePicker();
        startTimePicker = new TimePicker();
        absentField = new IntegerField();
        projectsDropdown = new ComboBox<>("Projects");
        description = new TextField("Description");
        minutesField = new IntegerField("Minutes");
        entryEditPanelFooter = new HorizontalLayout();
        upperEditFields = new FormLayout();
        lowerEditFields = new FormLayout();

        setId("workhours-edit-panel");

        createLowerEditFields(getAllProjects.get());
        createUpperEditFields(getAllEmployees.get());

        saveButton = new Button("Add");
        resetButton = new Button("Reset");
        deleteButton = new Button("Delete");

        deleteButton.addClassName(LumoUtility.Display.HIDDEN);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        entryEditPanelFooter.add(deleteButton, resetButton, saveButton);

        entryEditPanelFooter
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        entryEditPanelFooter.setWidth("100%");

        bindFields(binder);

        add(upperEditFields, lowerEditFields, entryEditPanelFooter);
    }

    private void createUpperEditFields(List<Employee> employeeList) {
        upperEditFields.setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("300px", 2), new ResponsiveStep("500px", 4));

        employeeDropdown.setPrefixComponent(new Icon("vaadin", "user"));
        employeeDropdown.setPlaceholder("Employee");
        employeeDropdown.setItems(employeeList);
        employeeDropdown.setRequired(true);
        employeeDropdown.setRequiredIndicatorVisible(false);

        datePicker.setRequired(true);
        datePicker.setRequiredIndicatorVisible(false);
        datePicker.setPlaceholder("Date");

        startTimePicker.setRequired(true);
        startTimePicker.setRequiredIndicatorVisible(false);
        startTimePicker.setPlaceholder("Arrival");

        absentField.setStep(15);
        absentField.setStepButtonsVisible(true);
        absentField.setRequired(true);
        absentField.setRequiredIndicatorVisible(false);
        absentField.setPlaceholder("Absent");

        upperEditFields.add(employeeDropdown, datePicker, startTimePicker,
                absentField);
        upperEditFields.addClassName("form-control-upper-fields");

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

    public void setDefaults() {
        absentField.setValue(30);
    }
    /*
     * @param type String for selecting which button to return. Possible values
     * are SAVE, DELETE or RESET.
     *
     * @return the corresponding button
     */
    // public Button getButton(String type) {
    // switch(type) {
    // case "SAVE": return this.saveButton;
    // case "RESET": return this.resetButton;
    // case "DELETE": return this.deleteButton;
    // default: return null;
    // }
    // }

}
