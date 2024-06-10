package com.example.application.data.services;

import com.example.application.data.models.Employee;
import com.example.application.data.models.Project;
import com.example.application.data.models.WorkLog;
import java.util.Optional;

import jakarta.transaction.Transactional;

import com.example.application.data.EmployeeRepository;
import com.example.application.data.ProjectRepository;
import com.example.application.data.WorkLogRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    public Employee getEmployee(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Transactional
    public void saveWorkLog(WorkLog newLog) {
        workLogRepository.save(newLog);
        workLogRepository.syncArrivalTimes(newLog.getStartDate(),
                newLog.getStartTime(), newLog.getEmployee());
        workLogRepository.syncAbsentTimes(newLog.getStartDate(),
                newLog.getAbsent(), newLog.getEmployee());
    }

    public void deleteOne(WorkLog workLog) {
        workLogRepository.delete(workLog);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<WorkLog> getAllTimes() {
        return workLogRepository.findAll();
    }

    public List<Integer> getTimesForDay(LocalDate date, long employee_id) {
        List<WorkLog> workLogs = workLogRepository.getMinutesForDay(date,
                employee_id);
        List<Integer> minutesList = new ArrayList<>();
        for (WorkLog logEntry : workLogs)
            minutesList.add(logEntry.getMinutes());
        return minutesList;
    }

}
