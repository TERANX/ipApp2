package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.validation.UserMapper;
import com.example.springsecurity.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service// тот же компонент
@RequiredArgsConstructor
public class UserService implements UserServiceInter{

    private BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    private final UserRepository repo;

    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repo.findAll();
        return users.stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

//    public User getById(Long id) {
//        return repo.findById(id).orElseThrow(
//                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
//    }
//
//    public User save (User user) {
//        return repo.save(user);
//    }
//
//    public User delete (Long id) {
//        User user = getById(id);
//        repo.delete(user);
//        return user;
//    }


    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setPassword(encoder().encode(userDto.getPassword()));

        // преобразование userdto в юзер. ЭТО для вздаимодействия клиента и сервера
        User user = UserMapper.mapToUser(userDto);

        User savedUser = repo.save(user);

        // наоборот
        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);

        return savedUserDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> optionalUser = repo.findById(userId);
        User user = optionalUser.get();
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto user) {
        User existingUser = repo.findById(user.getId()).get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = repo.save(existingUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        repo.deleteById(userId);
    }
}
