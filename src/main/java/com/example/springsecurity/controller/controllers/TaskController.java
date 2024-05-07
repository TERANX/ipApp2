package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.Options;
import com.example.springsecurity.model.Task;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.OptionsService;
import com.example.springsecurity.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class TaskController {

    @Autowired
    private TaskService tsi;

    @Autowired
    private OptionsService ops;

    @GetMapping("/tasks_list")
    public String getTasksList(Model model){
        model.addAttribute("tasks", tsi.findAllTasks());
        return "tasks_list";
    }

    @GetMapping("/setTask")
    public String getNewTaskPage(Model model){
        model.addAttribute("tasks", new Task());
        model.addAttribute("options", new Options());
        return "setTask";
    }

    @PostMapping("/setTask")
    public String createTask(@ModelAttribute("taskForm") @Valid Task taskForm ,
                             @ModelAttribute("optionForm") @Valid Options optionForm) throws EmailExistsException {
        tsi.save(taskForm);
        ops.save(optionForm);
        return "redirect:/tasks_list";
    }

    //Options

//    @GetMapping("/options_list")
//    public String getOptList(Model model){
//        model.addAttribute("options_list", ops.getAll());
//        return "options_list";
//    }

//    @GetMapping("/setTask")
//    public String getOptions(Model model) {
//        model.addAttribute("options", new Options());
//        return "setTask";
//    }
}
