package com.example.application.data.models;

import com.example.application.data.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "employees")
public class Employee extends AbstractEntity {
    
    @NotEmpty
    @Column(name = "first_name")
    private String firstName = "";
    
    @NotEmpty
    @Column(name = "last_name")
    private String lastName = "";

    public String getName() {
        return firstName + " " + lastName;
    }
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
} 
