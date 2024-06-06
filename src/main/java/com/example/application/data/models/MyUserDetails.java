package com.example.application.data.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
    private String username;
    private String password;
    private String role;
    private long employeeId;
    private List<GrantedAuthority> grantedAuthorities;

    public MyUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.employeeId = user.getEmployeeId();
        this.grantedAuthorities = Arrays
                .asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isAccountNonExpired() {
        return (true);
    }

    public boolean isAccountNonLocked() {
        return (true);
    }

    public boolean isCredentialsNonExpired() {
        return (true);
    }

    public boolean isEnabled() {
        return (true);
    };
    public long getEmployeeId() {
        return employeeId;
    }
}
