package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="taskSolutions")
@Data

public class TaskSolutions {
    @Id
    @GeneratedValue
    private Long id;

    private String firstSolution;

    private String secondSolution;

    private String thirdSolution;

    private Integer numberOfCorrectSolution;

    @OneToOne
    @JoinColumn(name = "task_id", unique = true, nullable = false)
    private Task task;
}
