package com.example.application.data.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.application.data.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "times")
public class WorkLog extends AbstractEntity {
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @NotNull
    @JsonIgnoreProperties({"employees"})
    private Employee employee;
    
    @NotEmpty
    @Column(name = "start_time")
    private LocalTime startTime;

    @NotEmpty
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @NotEmpty
    @Column(name = "minutes")
    private int minutes;

    @NotEmpty
    @Column(name = "absent")
    private int absent;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull
    @JsonIgnoreProperties({"projects"})
    private Project project;

    @Override
    public String toString() {
        return project.toString();
    }

    public String getEmployeeName() { return employee.toString(); }

    public Employee getEmployee() {System.out.println(this.employee); return employee;  }
    public void setEmployee(Employee employee) { this.employee = employee; System.out.println(this.employee);}
  
    public void setStartTime(LocalTime starTime) { this.startTime = startTime; }
    public LocalTime getStartTime() { return startTime; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public int getAbsent() { return absent;}
    public void setAbsent(int absent) { this.absent = absent;}

    public String getDescription() { return description; }
    public String getProjectName() { return project.toString(); }
    public Project getProject() { return project; }

    public void initProject() { this.project = new Project(); }

    public void setProjectName(String name) { System.out.println(name); this.project.setName(name); }
    public int getMinutes() { return minutes;}
    
} 
