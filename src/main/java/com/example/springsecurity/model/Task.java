package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String condition;

    @OneToMany(mappedBy = "task")
    private List<CompletingTask> completingTasks;

    public Task(Integer id, String name, String condition) {
        this.id = id;
        this.name = name;
        this.condition = condition;
    }
}

