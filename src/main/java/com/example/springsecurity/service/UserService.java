package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service// тот же компонент
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repo;

    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
    }

    public void addUser(User user){
        user.setPassword(encoder().encode(user.getPassword()));
        repo.save(user);
    }

//    public User save (User user) {
//        return repo.save(user);
//    }


    public User delete (Long id) {
        User user = getById(id);
        repo.delete(user);
        return user;
    }

}
