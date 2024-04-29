package com.example.springsecurity.repository;

import com.example.springsecurity.model.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();
}
