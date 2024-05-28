package com.example.application.data.services;

import com.example.application.data.models.Employee;
import com.example.application.data.models.TimeEntry;

import com.example.application.data.EmployeeRepository;
import com.example.application.data.ProjectRepository;
import com.example.application.data.TimesRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class TimesService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final TimesRepository timesRepository;

    public TimesService(EmployeeRepository employeeRepository,
                        ProjectRepository projectRepository,
                        TimesRepository timesRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.timesRepository = timesRepository;
    }

    public long countEmployees() {
        return employeeRepository.count();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public List<TimeEntry> getAllTimes() {
        return timesRepository.findAll();
    }
    
}
