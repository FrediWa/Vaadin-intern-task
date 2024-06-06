package com.example.application.data.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.application.data.UserRepository;
import com.example.application.data.models.MyUserDetails;
import com.example.application.data.models.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MyUserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        System.out.println("Doing the things");
        User user = userRepository.findByUsername(username);
        return (new MyUserDetails(user));
    }
}
