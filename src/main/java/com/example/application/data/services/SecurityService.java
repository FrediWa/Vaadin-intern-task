package com.example.application.data.services;

import com.example.application.data.models.MyUserDetails;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public MyUserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(MyUserDetails.class)
                .get();
    }

    public void logout() {
        authenticationContext.logout();
    }
}