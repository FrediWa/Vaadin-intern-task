package com.example.application.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.timepicker.TimePicker;

import com.example.application.data.models.Project;
import com.example.application.data.models.Employee;
import com.example.application.data.models.WorkLog;

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
    Dialog              deleteDialog        = new Dialog();
    Button              deleteButton        = new Button("Delete", e -> deleteDialog.open());
    Button              confirmButton       = new Button("Delete", e -> {handleDelete(); deleteDialog.close();});
    Button              cancelButton        = new Button("Cancel", e -> deleteDialog.close());
    long                workLogId;

    private void updateAllowEdit(boolean readonly) {
        employeeDropdown.setReadOnly(readonly);
        projectDropdown.setReadOnly(readonly);
        startTimePicker.setReadOnly(readonly);
        endTimePicker.setReadOnly(readonly);
        datePicker.setReadOnly(readonly);
    }

    private void handleDelete() {
        // ...
    }

    private void handleEdit() {
        // ...
    }

    Checkbox readOnlySelector = new Checkbox();
    // Ok what is up with this
    // readOnlySelector.setLabel("Edit");
    boolean readonly = !(readOnlySelector.getValue());
    
    public WorkLogRow(WorkLog workLogEntry, List<Employee> employees, List<Project> projects) {
        workLogId = workLogEntry.getId();

        deleteDialog.setHeaderTitle("Confirm entry deletion");
        deleteDialog.getFooter().add(cancelButton);
        deleteDialog.getFooter().add(confirmButton);

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

        employeeDropdown.setReadOnly(readonly);
        projectDropdown.setReadOnly(readonly);
        startTimePicker.setReadOnly(readonly);
        endTimePicker.setReadOnly(readonly);
        datePicker.setReadOnly(readonly);

        readOnlySelector.addValueChangeListener(e -> {
            readonly = !(e.getValue());
            updateAllowEdit(readonly);
        });

        getContent().add(projectDropdown, 
            employeeDropdown,
            startTimePicker,
            endTimePicker,
            datePicker,
            deleteButton,
            readOnlySelector);
    }

}
