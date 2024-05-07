package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="options")
@Data
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOptions")
    private Long idOptions;

    @ManyToOne
    @JoinColumn(name = "idTask")
    private Task taskId;

    private Character localNum;

    private String varAnswer;

    private boolean valid;



}
