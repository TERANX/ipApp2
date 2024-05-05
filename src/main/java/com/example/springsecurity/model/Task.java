package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTask")
    private Long idTask;
    private String name;
    private String condition;

    @OneToMany(mappedBy = "taskId")
    private List<Options> options;

    @OneToMany(mappedBy = "task")
    private List<CompletingTask> completingTasks;

}

