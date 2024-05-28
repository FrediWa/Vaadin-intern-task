package com.example.application.data.models;

import com.example.application.data.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.example.application.data.models.Project;

@Entity
@Table(name = "times")
public class TimeEntry extends AbstractEntity {
    
    @NotEmpty
    @Column(name = "employee_id")
    private int employeeId;
    
    @NotEmpty
    @Column(name = "start_date")
    private String startDate = "";

    @NotEmpty
    @Column(name = "start_time")
    private String startTime = "";

    @NotEmpty
    @Column(name = "end_time")
    private String endTime = "";

     @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull
    @JsonIgnoreProperties({"projects"})
    private Project project;

    @Override
    public String toString() {
        return project.toString();
    }
} 
