package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String address;
    private String roles;
    @OneToMany(mappedBy = "user")
    private List<CompletingTask> completingTasks;
}

