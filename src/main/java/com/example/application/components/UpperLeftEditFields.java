package com.example.application.components;

import com.example.application.data.models.Employee;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.util.List;

public class UpperLeftEditFields extends FormLayout {
    WorkLogService service;
    ComboBox<Employee> employeeDropdown;
    DatePicker datePicker;
    TimePicker startTimePicker;
    IntegerField absentField;

    public UpperLeftEditFields(BeanValidationBinder<WorkLog>  binder,  WorkLogService service) {
        addClassName("contact-form");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("300px", 2),
                new ResponsiveStep("500px", 4)
        );
        this.service = service;

        binder.bindInstanceFields(this);

        employeeDropdown = new ComboBox<>();
        employeeDropdown.setLabel("Employee");

        datePicker = new DatePicker();
        startTimePicker = new TimePicker();
        datePicker.setLabel("Date");
        startTimePicker.setLabel("Start time");

        employeeDropdown.setPrefixComponent(new Icon("vaadin", "user"));
        employeeDropdown.setItems(service.getAllEmployees());

        absentField = new IntegerField();
        absentField.setLabel("Absent");
        absentField.setStep(15);
        absentField.setValue(30);
        absentField.setStepButtonsVisible(true);

        // binder.bind(employeeDropdown, WorkLog::getEmployee, WorkLog::setEmployee);
        // binder.bind(datePicker, WorkLog::getStartDate, WorkLog::setEmployee);
        // binder.bind(startTimePicker, WorkLog::getStartTime, WorkLog::setEmployee);
        // binder.bind(absentField, WorkLog::getAbsent, WorkLog::setAbsent);

        add(employeeDropdown, datePicker, startTimePicker, absentField);

        

        // Rest of constructor omitted
    }
}
