package com.example.springsecurity.validation;

import com.example.springsecurity.model.User;
import com.example.springsecurity.web.dto.UserDto;

public class UserMapper {

    // преобразование userdto в юзер
    public static UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
        );
        return userDto;
    }

    // наоборот
    public static User mapToUser(UserDto userDto){
        User user = new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRoles()
        );
        return user;
    }

}
