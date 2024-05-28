package com.example.application.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import com.example.application.data.models.Project;
import com.example.application.data.models.Employee;
import com.example.application.data.models.TimeEntry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkLogRow extends Composite<Div> {
    
    ComboBox<Project>   projectDropdown     = new ComboBox<Project>();
    ComboBox<Employee>  employeeDropdown    = new ComboBox<Employee>();
    TimePicker          startTimePicker     = new TimePicker();
    TimePicker          endTimePicker       = new TimePicker();
    DatePicker          datePicker          = new DatePicker();
    LocalTime           startTime;
    LocalTime           endTime;
    
    Checkbox readOnlySelector = new Checkbox();
    // readOnlySelector.setLabel("Edit fields");
    boolean readOnly = readOnlySelector.getValue();
    
    public WorkLogRow(TimeEntry workLogEntry, List<Employee> employees, List<Project> projects) {
        employeeDropdown.setItems(employees);
        projectDropdown.setItems(projects);
        
        employeeDropdown.setValue(workLogEntry.getEmployee());
        projectDropdown.setValue(workLogEntry.getProject());
        datePicker.setValue(LocalDate.parse(workLogEntry.getStartDate()));
        
        startTime = LocalTime.parse(workLogEntry.getStartTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        endTime   = LocalTime.parse(workLogEntry.getEndTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        startTimePicker.setValue(startTime);
        endTimePicker.setValue(endTime);
        startTimePicker.setStep(Duration.ofMinutes(15));
        endTimePicker.setStep(Duration.ofMinutes(15));

        employeeDropdown.setReadOnly(readOnly);
        projectDropdown.setReadOnly(readOnly);

        getContent().add(projectDropdown, 
            employeeDropdown,
            startTimePicker,
            endTimePicker,
            datePicker,
            readOnlySelector);
    }

}
