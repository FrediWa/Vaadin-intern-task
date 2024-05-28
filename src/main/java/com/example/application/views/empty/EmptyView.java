package com.example.application.views.empty;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.TimeEntry;
import com.example.application.data.services.TimesService;

import java.util.List;

@PageTitle("Empty")
@Route(value = "")
@RouteAlias(value = "")
public class EmptyView extends VerticalLayout {
    TimesService service;

    public EmptyView(TimesService service) {
        this.service = service;
        List<Employee> employeeList = service.getAllEmployees(); 
        List<TimeEntry> timesList = service.getAllTimes();
        System.out.println(timesList.get(1).toString());
        
    

        System.out.println(employeeList.get(0).getName());
        Employee[] array = new Employee[employeeList.size()];
        employeeList.toArray(array);
        Select<Employee> select = new Select<>();
        select.setLabel("Sort by");
        select.setItems(array);

        add(select);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
