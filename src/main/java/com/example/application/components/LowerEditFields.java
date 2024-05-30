package com.example.application.components;

import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

public class LowerEditFields extends FormLayout {
    private WorkLogService service;
    public ComboBox<Project> projectsDropdown;
    
    public LowerEditFields(BeanValidationBinder<WorkLog>  binder,  WorkLogService service) {
        projectsDropdown = new ComboBox<>("Projects");
        TextField description = new TextField("Description");
        IntegerField minutesField = new IntegerField("Minutes");
        minutesField.setStep(15);
        minutesField.setStepButtonsVisible(true);
        projectsDropdown.setItems(service.getAllProjects());

        setColspan(description, 3);
        setResponsiveSteps(
            new ResponsiveStep("0", 3),
            new ResponsiveStep("300px", 5)
        );
        
        add(minutesField, description, projectsDropdown);

        binder.bind(projectsDropdown, WorkLog::getProject, WorkLog::setProject);
        binder.bind(description, WorkLog::getDescription, WorkLog::setDescription);
        binder.bind(minutesField, WorkLog::getMinutes, WorkLog::setMinutes);
    }

}
