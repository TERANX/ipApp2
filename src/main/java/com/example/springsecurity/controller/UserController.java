package com.example.springsecurity.controller;

import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import com.example.springsecurity.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/users/{id}")
//    public User getById(@PathVariable Long id) {
//        return service.getById(id);
//    }
//    @PostMapping("/users")
//    public User save (@RequestBody User user) {
//        return service.save(user);
//    }
//    @DeleteMapping("/users/{id}")
//    public User delete (@PathVariable Long id) {
//        return service.delete(id);
//    }


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user){
        UserDto savedUser = service.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
        UserDto user = service.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                              @RequestBody UserDto user){
        user.setId(userId);
        UserDto updatedUser = service.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        service.deleteUser(userId);
        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }

}
