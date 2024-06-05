package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.Option;
import com.example.springsecurity.model.Task;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.OptionsRepository;
import com.example.springsecurity.repository.TaskRepository;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class TaskController {

    @Autowired
    TaskRepository tr;

    @Autowired
    private TaskService tsi;

    @Autowired
    private OptionsService ops;

    @Autowired
    OptionsRepository or;

    @GetMapping("tasks_list")
    public String getTasksList(Model model){
        model.addAttribute("tasks", tsi.findAllTasks());
        return "tasks_list";
    }

    @GetMapping("/setTask")
    public String getNewTaskPage(Model model){
        model.addAttribute("tasks", new Task());
        model.addAttribute("options", new Option());
        model.addAttribute("option", ops.getAll());
        return "setTask";
    }

    @PostMapping("/setTask")
    public String createTask(@ModelAttribute("taskForm") @Valid Task taskForm, Option optForm){
        tsi.save(taskForm, optForm);

//        tr.save(taskForm);
//        or.save(optForm);


        return "redirect:/tasks_list";
    }

    //Options

//    @GetMapping("setTask")
//    public String getOptList(Model model){
//        model.addAttribute("options", ops.getAll());
//        return "setTask";
//    }
//
//    @PostMapping("/setOptions")
//    public String createOptions(@ModelAttribute("optForm") @Valid Option optForm){
////        optForm.setLocalNum("a");
//        ops.save(optForm);
//        return "tasks_list";
//    }


}
