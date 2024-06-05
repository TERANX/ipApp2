package com.example.springsecurity.model.dto;


import com.example.springsecurity.model.Option;
import com.example.springsecurity.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTaskDto {

    private Long id;

    private Task task;



    private Option option;






}
