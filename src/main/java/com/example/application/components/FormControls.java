package com.example.application.components;

import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.AbstractNumberField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.theme.lumo.LumoUtility;

@CssImport("./themes/intern-project/components/editPanel.css")
public class FormControls extends VerticalLayout {
    public ComboBox<Project> projectsDropdown;
    ComboBox<Employee> employeeDropdown;
    DatePicker datePicker;
    TimePicker startTimePicker;
    FormLayout upperEditFields;
    FormLayout lowerEditFields;
    TextField description;
    IntegerField minutesField;
    IntegerField absentField;
    WorkLogService service;
    public Button saveButton;
    public Button resetButton;
    public Button deleteButton;

    public FormControls(BeanValidationBinder<WorkLog>  binder,  WorkLogService service) {
        setId("workhours-edit-panel");
        this.service = service;
        lowerEditFields = createLowerEditFields();
        upperEditFields = createUpperEditFields();
        
        HorizontalLayout entryEditPanelFooter = new HorizontalLayout();
        saveButton = new Button("Add");
        resetButton = new Button("Reset");
        deleteButton = new Button("Delete");

    
        deleteButton.addClassName(LumoUtility.Display.HIDDEN);


        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        entryEditPanelFooter.add(deleteButton, resetButton, saveButton);

        entryEditPanelFooter.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        entryEditPanelFooter.setWidth("100%");

        bindFields(binder);

        
        add(upperEditFields, lowerEditFields, entryEditPanelFooter);

    }

    private FormLayout createUpperEditFields() {
        FormLayout upperEditFields = new FormLayout();
        upperEditFields.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("300px", 2),
                new ResponsiveStep("500px", 4)
        );

        employeeDropdown = new ComboBox<>();
        employeeDropdown.setPrefixComponent(new Icon("vaadin", "user"));
        employeeDropdown.setPlaceholder("Employee");
        employeeDropdown.setItems(service.getAllEmployees());
        employeeDropdown.setRequired(true);
        employeeDropdown.setRequiredIndicatorVisible(false);

        datePicker = new DatePicker();
        datePicker.setRequired(true);
        datePicker.setRequiredIndicatorVisible(false);
        datePicker.setPlaceholder("Date");

        
        startTimePicker = new TimePicker();
        startTimePicker.setRequired(true);
        startTimePicker.setRequiredIndicatorVisible(false);
        startTimePicker.setPlaceholder("Arrival");

        absentField = new IntegerField();

        absentField.setStep(15);
        absentField.setValue(30);
        absentField.setStepButtonsVisible(true);
        absentField.setRequired(true);
        startTimePicker.setPlaceholder("Absent");
        absentField.setRequiredIndicatorVisible(false);


        upperEditFields.add(employeeDropdown, datePicker, startTimePicker, absentField);
        

        upperEditFields.addClassName("form-control-upper-fields");

        return(upperEditFields);
    }
    private FormLayout createLowerEditFields() {
        FormLayout lowerEditFields = new FormLayout();
        projectsDropdown = new ComboBox<>("Projects");
        description = new TextField("Description");
        minutesField = new IntegerField("Minutes");
        minutesField.setStep(15);
        minutesField.setStepButtonsVisible(true);
        projectsDropdown.setItems(service.getAllProjects());

        lowerEditFields.setColspan(description, 3);
        lowerEditFields.setResponsiveSteps(
            new ResponsiveStep("0", 3),
            new ResponsiveStep("300px", 5)
        );
        
        lowerEditFields.add(minutesField, description, projectsDropdown);
        lowerEditFields.addClassName("form-control-lower-fields");
        return(lowerEditFields);
    }
    private void bindFields(BeanValidationBinder<WorkLog>  binder) {
        binder.bind(projectsDropdown, WorkLog::getProject, WorkLog::setProject);
        binder.bind(description, WorkLog::getDescription, WorkLog::setDescription);
        binder.bind(minutesField, WorkLog::getMinutes, WorkLog::setMinutes);
        binder.bind(employeeDropdown, WorkLog::getEmployee, WorkLog::setEmployee);
        binder.bind(datePicker, WorkLog::getStartDate, WorkLog::setStartDate);
        binder.bind(startTimePicker, WorkLog::getStartTime, WorkLog::setStartTime);
        binder.bind(absentField, WorkLog::getAbsent, WorkLog::setAbsent);
    }
    public void setDefaults() {
        absentField.setValue(30);
    }
    
}
