package com.example.application.data.models;

import com.example.application.data.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "projects")
public class Project extends AbstractEntity {
    
    @NotEmpty
    @Column(name = "project_name")
    private String project = "";

    @Override
    public String toString() {
        return project;
    }

    public String getName() {
        return this.project;
    }
    public void setName(String name) {
        this.project = name;
        System.out.println(this.project);
    }
} 
