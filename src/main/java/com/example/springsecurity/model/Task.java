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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTask")
    private Long idTask;
    private String title;
    private String description;
    private String difficulty;

    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL)
    private List<Options> options;

    @ElementCollection
    private List<Boolean> correctOption;

//    @OneToMany(mappedBy = "task")
//    private List<CompletingTask> completingTasks;

}

