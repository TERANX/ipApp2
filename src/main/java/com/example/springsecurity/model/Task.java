package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String condition;

    @OneToMany(mappedBy = "task")
    private List<CompletingTask> completingTasks;

    public Task(int id, String name, String condition) {
        this.id = (long) id;
        this.name = name;
        this.condition = condition;
    }
}

