package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegController {

    private UserService userService;



}
