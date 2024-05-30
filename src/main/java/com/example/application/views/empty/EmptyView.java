package com.example.application.views.empty;

import com.example.application.components.UpperLeftEditFields;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.router.BeforeEnterEvent;
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

import org.apache.commons.lang3.ObjectUtils.Null;
import org.aspectj.lang.annotation.Before;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Empty")
@Route(value = "/:employeeId?/:action?(edit)")
@RouteAlias(value = "")
public class EmptyView extends VerticalLayout {
    WorkLogService service;
    private final BeanValidationBinder<WorkLog> binder;
    UpperLeftEditFields upperLeftEditFields;
    private WorkLog currentWorkLog;

    public EmptyView(WorkLogService service) {
        this.service = service;
        binder = new BeanValidationBinder<>(WorkLog.class);

        upperLeftEditFields = new UpperLeftEditFields(binder, service);

        VerticalLayout entryEditPanel = new VerticalLayout();
        HorizontalLayout upperEditFields = new HorizontalLayout();
        FormLayout upperRightEditFields = new FormLayout();
        FormLayout lowerEditFields = new FormLayout();

        

        
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
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) 
                UI.getCurrent().navigate(String.format("/%s/edit", event.getValue().getId()));
            else
                UI.getCurrent().navigate(EmptyView.class);
        });

        updateButton.addClickListener(e -> {
            try {
                System.out.println("Hello");
                if (this.currentWorkLog == null) {
                    System.out.println("Null");
                    this.currentWorkLog = new WorkLog();
                }
                binder.writeBean(this.currentWorkLog);

                //samplePersonService.update(this.samplePerson);
                //clearForm();
                //refreshGrid();
                System.out.println("hello");
                service.updateOneWorkLog(currentWorkLog);
                Notification.show("Data updated");
                UI.getCurrent().navigate(EmptyView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {

            } catch (ValidationException validationException) {

            }
        });

        // Binder<WorkLog> binder = new Binder<>(WorkLog.class);

        // TextField nameField = new TextField();

        // binder.bind(nameField, WorkLog::getProjectName, WorkLog::setProjectName);

        // WorkLog entry = new WorkLog();
        // entry.initProject();
        // entry.setProjectName("Hello World");


        // Button btn = new Button("Save",
        // event -> {
        //     try {
        //         binder.writeBean(entry);
        //         System.out.println("Bean written");
        //         // A real application would also save
        //         // the updated person
        //         // using the application's backend
        //     } catch (ValidationException e) {
        //         // notifyValidationException(e);
        //     }
        // });

        // // Updates the fields again with the
        // // previously saved values
        // Button resetButton = new Button("Reset", event -> {
        //     binder.readBean(entry);
        // });

        // add(nameField, btn, resetButton);
        
        add(entryEditPanel);
        // binder.bindInstanceFields(this);

        add(grid);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
