package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import com.example.springsecurity.web.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegController {

    private UserService userService;

    @GetMapping("/reg")
    public String registration(Model model){
        model.addAttribute("user", new UserDto());
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("user") @Valid UserDto userDto, Model model){
        userService.createUser(userDto);
        return "redirect:/login";
    }

}
