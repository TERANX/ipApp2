package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.Task;
import com.example.springsecurity.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class TaskController {
    private final TaskService taskService;

    @GetMapping("tasks_list")
    public String getTasksList(Model model){
        model.addAttribute("tasks", this.taskService.findAllTasks());
        return "tasks_list";
    }

    @GetMapping("create_task")
    public String getNewTaskPage(){
        return "new_task";
    }

    @PostMapping("create_task")
    public String createTask(NewTask newTask) {
        Task task = this.taskService.createTask(newTask.name(), newTask.condition());
        return "redirect:tasks_list";
    }

}
