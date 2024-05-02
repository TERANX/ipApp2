package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="completingTasks")
@Data
public class CompletingTask {

    @Id
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;

    boolean statusDone; // предоставлено ли решение задачи или еще нет

    boolean statusTrue; // правильно сделана задача или нет

    LocalDateTime registeredAt; // дата решения задачи

    int grade; // оценка преподавателя за решение задачи
}
