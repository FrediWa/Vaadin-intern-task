package com.example.application.data.services;

import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;

import com.example.application.data.EmployeeRepository;
import com.example.application.data.ProjectRepository;
import com.example.application.data.WorkLogRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class WorkLogService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkLogRepository workLogRepository;

    public WorkLogService(EmployeeRepository employeeRepository,
                          ProjectRepository projectRepository,
                          WorkLogRepository timesRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.workLogRepository = timesRepository;
    }

    public long countEmployees() {
        return employeeRepository.count();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public List<WorkLog> getAllTimes() {
        return workLogRepository.findAll();
    }
    
}
