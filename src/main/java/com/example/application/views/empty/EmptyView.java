package com.example.application.views.empty;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import com.example.application.data.services.WorkLogService;

import com.example.application.components.WorkLogRow;

import java.util.List;

@PageTitle("Empty")
@Route(value = "")
@RouteAlias(value = "")
public class EmptyView extends VerticalLayout {
    WorkLogService service;

    public EmptyView(WorkLogService service) {
        this.service = service;
        List<Employee> employeeList = service.getAllEmployees(); 
        List<Project> projectList = service.getAllProjects(); 
        List<WorkLog> workLogList = service.getAllTimes();

        VirtualList<WorkLog> list = new VirtualList<>();

        ComponentRenderer<Component, WorkLog> workLogRenderer = new ComponentRenderer<>(workLogEntry -> {
            return(new WorkLogRow(workLogEntry, employeeList, projectList));
        });

        list.setItems(workLogList);
        list.setRenderer(workLogRenderer);
        add(list);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
