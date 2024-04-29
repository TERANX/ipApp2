package com.example.springsecurity.repository;

import com.example.springsecurity.model.Task;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public TaskRepositoryImpl(){
        IntStream.range(1, 4)
                .forEach(i->this.tasks.add(new Task(i, "Задача №%d".formatted(i), "Условие задачи №%d".formatted(i))));
    }

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }
}
