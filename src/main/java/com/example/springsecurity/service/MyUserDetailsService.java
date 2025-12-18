package com.example.springsecurity.service;

import com.example.springsecurity.model.MyUserDetails;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("=== MyUserDetailsService.loadUserByUsername() ===");
        System.out.println("Loading user: " + userName);

        Optional<User> user = repository.findByName(userName);

        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getName());
            System.out.println("User email: " + user.get().getEmail());

            if (user.get().getRoles() != null) {
                System.out.println("User roles: " +
                        user.get().getRoles().stream()
                                .map(role -> role.getName())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("No roles"));
            }

            return user.map(MyUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException(userName + " not found"));
        } else {
            System.err.println("User not found: " + userName);
            throw new UsernameNotFoundException(userName + " not found");
        }
    }
}