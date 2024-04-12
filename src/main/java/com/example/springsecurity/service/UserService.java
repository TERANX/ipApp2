package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service// тот же компонент
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository repo;

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
    }

    public User save (User user) {
        return repo.save(user);
    }

    public User delete (Long id) {
        User user = getById(id);
        repo.delete(user);
        return user;
    }



}
