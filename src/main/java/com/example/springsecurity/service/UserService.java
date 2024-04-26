package com.example.springsecurity.service;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service// тот же компонент
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private  final UserRepository repo;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder encoder;


    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
    }

    public User save (User user) throws EmailExistsException {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }


    public User delete (Long id) {
        User user = getById(id);
        repo.delete(user);
        return user;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :id", User.class)
                .setParameter("id", idMin).getResultList();
    }


}
