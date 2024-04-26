package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.getAll());
        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.delete(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/all")
    public String showAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin";
    }

    @GetMapping("/admin/{id}")
    public String gtUser(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("users", userService.usergtList(userId));
        return "admin";
    }
}