package com.example.springsecurity.service;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.Task;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.TaskRepository;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private  final TaskRepository trepo;

    public List<Task> findAllTasks() {
        return trepo.findAll();
    }

    public Task getById(Long id) {
        return trepo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
    }

    public Task save (Task task) throws EmailExistsException {
        return trepo.save(task);
    }


    public Task delete (Long id) {
        Task task = getById(id);
        trepo.delete(task);
        return task;
    }




}
