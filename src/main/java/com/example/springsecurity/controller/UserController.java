package com.example.springsecurity.controller;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users")
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id);
    }
    @PostMapping("/users")
    public User save (@RequestBody User user) throws EmailExistsException {
        return service.save(user);
    }
    @DeleteMapping("/users/{id}")
    public User delete (@PathVariable Long id) {
        return service.delete(id);
    }

}
