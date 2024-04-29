package com.example.springsecurity.service;

import com.example.springsecurity.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllTasks();
}
