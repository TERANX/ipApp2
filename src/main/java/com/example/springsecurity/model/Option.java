package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="options")
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOptions")
    private Long idOptions;

    @ManyToOne
    @JoinColumn(name = "idTask")
    private Task taskId;

//    private Character localNum;

    private String option;

    private Boolean corOpt;

}
