package com.example.application.data.models;

import com.example.application.data.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @NotEmpty
    @Column(name = "username")
    private String username = "";

    @NotEmpty
    @Column(name = "role")
    private String role = "";

    @NotEmpty
    @Column(name = "password")
    private String password = "";

    public @NotEmpty String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty String username) {
        this.username = username;
    }

    public @NotEmpty String getRole() {
        return role;
    }

    public void setRole(@NotEmpty String role) {
        this.role = role;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }
}
