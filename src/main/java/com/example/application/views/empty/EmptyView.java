package com.example.application.views.empty;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;

import com.example.application.components.WorkLogRow;

import java.time.LocalTime;
import java.util.List;

@PageTitle("Empty")
@Route(value = "")
@RouteAlias(value = "")
public class EmptyView extends VerticalLayout {
    WorkLogService service;

    public EmptyView(WorkLogService service) {
        this.service = service;

        VerticalLayout entryEditPanel = new VerticalLayout();
        HorizontalLayout upperEditFields = new HorizontalLayout();
        FormLayout upperLeftEditFields = new FormLayout();
        FormLayout upperRightEditFields = new FormLayout();
        FormLayout lowerEditFields = new FormLayout();
        upperLeftEditFields.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("300px", 2),
            new ResponsiveStep("500px", 4)
        );

        ComboBox<Employee> employeeDropdown = new ComboBox<>();
        employeeDropdown.setLabel("Employee");

        DatePicker datePicker = new DatePicker();
        TimePicker startTimePicker = new TimePicker();
        datePicker.setLabel("Date");
        startTimePicker.setLabel("Start time");

        employeeDropdown.setPrefixComponent(new Icon("vaadin", "user"));
        employeeDropdown.setItems(service.getAllEmployees());

        upperLeftEditFields.add(employeeDropdown);
        upperLeftEditFields.add(datePicker);
        upperLeftEditFields.add(startTimePicker);
        
        NumberField absentField = new NumberField();
        absentField.setLabel("Absent");
        absentField.setStep(15);
        absentField.setValue(30.0);
        absentField.setStepButtonsVisible(true);
        upperLeftEditFields.add(absentField);
        
        TimePicker departurePicker = new TimePicker();
        departurePicker.setLabel("Departure");
        departurePicker.setValue(LocalTime.of(7, 0));
        departurePicker.setReadOnly(true);

        TextField totalHours = new TextField();
        totalHours.setLabel("Total");
        totalHours.setValue("7h30min");
        totalHours.setReadOnly(true);

        upperRightEditFields.add(departurePicker, totalHours);

        upperRightEditFields.setResponsiveSteps(

            new ResponsiveStep("0", 1),
            new ResponsiveStep("300px", 2)
        );

        upperEditFields.add(upperLeftEditFields, upperRightEditFields);
        upperEditFields.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        upperEditFields.setWidth("100%");
        
        ComboBox<Project> projectsDropdown = new ComboBox<>("Projects");
        TextField description = new TextField("Description");
        NumberField minutesField = new NumberField("Minutes");
        minutesField.setStep(15);
        minutesField.setValue(15.0);
        minutesField.setStepButtonsVisible(true);
        projectsDropdown.setItems(service.getAllProjects());
        lowerEditFields.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("300px", 3)
        );
        lowerEditFields.add(projectsDropdown, description, minutesField);
        
        HorizontalLayout entryEditPanelFooter = new HorizontalLayout();
        Button saveButton = new Button("Save");
        Button updateButton = new Button("Update");
        entryEditPanelFooter.add(updateButton, saveButton);
        entryEditPanelFooter.setWidth("100%");
        entryEditPanelFooter.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        entryEditPanel.add(upperEditFields, lowerEditFields, entryEditPanelFooter);

        Grid<WorkLog> grid = new Grid<>(WorkLog.class, false);
        grid.addColumn(WorkLog::getEmployeeName).setHeader("Employee");
        grid.addColumn(WorkLog::getMinutes).setHeader("Minutes");
        grid.addColumn(WorkLog::getProjectName).setHeader("Project");
        grid.addColumn(WorkLog::getDescription).setHeader("Description");
        grid.addColumn(WorkLog::getStartDate).setHeader("Project");
   
        List<WorkLog> people = service.getAllTimes();
        grid.setItems(people);

        add(entryEditPanel);
        add(grid);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
