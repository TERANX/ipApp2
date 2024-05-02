package com.example.springsecurity.repository;

import com.example.springsecurity.model.Task;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.*;


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

    @Override
    public Task save(Task task) {
        task.setId(this.tasks.stream()
                .max(Comparator.comparingInt(Task::getId))
                .map(Task::getId)
                .orElse(0) + 1);
        this.tasks.add(task);
        return task;
    }
}
