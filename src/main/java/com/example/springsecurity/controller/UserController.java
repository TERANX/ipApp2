package com.example.springsecurity.controller;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/get/name")
    public ResponseEntity<String> getName() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User with id = " + id + " not found!")));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

//    @GetMapping("/users")
//    public List<User> getAll() {
//        return service.findAll();
//    }
//
//    @GetMapping("/users/{id}")
//    public User getById(@PathVariable Long id) {
//        return service.getById(id);
//    }
//
//    @PostMapping("/users")
//    public User save (@RequestBody User user) throws EmailExistsException {
//        return service.save(user);
//    }
//
//    @DeleteMapping("/users/{id}")
//    public User delete (@PathVariable Long id) {
//        return service.delete(id);
//    }

}
