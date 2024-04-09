package com.example.springsecurity.service;

import com.example.springsecurity.web.dto.UserDto;

import java.util.List;

public interface UserServiceInter {

    UserDto createUser(UserDto user);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user);

    void deleteUser(Long userId);

}
