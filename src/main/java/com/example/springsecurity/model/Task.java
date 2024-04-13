package com.example.springsecurity.model;



import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tasks")
@Data

public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String condition;

    @OneToMany(mappedBy = "task")
    private List<CompletingTask> completingTasks;

}
