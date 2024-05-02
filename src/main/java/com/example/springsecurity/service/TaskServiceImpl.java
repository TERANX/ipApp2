package com.example.springsecurity.service;

import com.example.springsecurity.model.Task;
import com.example.springsecurity.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Override
    public List<Task> findAllTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    public Task createTask(String name, String condition) {
        return this.taskRepository.save(new Task(null, name, condition));
    }
}
