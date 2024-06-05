package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTask")
    private Long idTask;
    private String title;
    private String description;
    private String difficulty;

    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL)
    private List<Option> options;

    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL)
    private List<Option> corOptions;

    public Task(Map<String, String> taskForm) {
        title = taskForm.remove("title");
        description = taskForm.remove("description");
        difficulty = taskForm.remove("difficulty");
    }

//    @OneToMany(mappedBy = "task")
//    private List<CompletingTask> completingTasks;

}

