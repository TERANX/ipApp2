package com.example.springsecurity.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
@Controller
@AllArgsConstructor
public class ControllerForm {

    @GetMapping("/")
    public String home() {
        return "redirect:/afterReg";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        return "index";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "registered", required = false) String registered,
                           Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out");
        }
        if (registered != null) {
            model.addAttribute("message", "Registration successful! Please login.");
        }

        return "login";
    }

    @GetMapping("/custom-login")
    public String customLogin(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @RequestParam(value = "registered", required = false) String registered,
                              Model model) {

        System.out.println("=== CUSTOM LOGIN PAGE ACCESSED ===");

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
            System.out.println("Login error parameter detected");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
            System.out.println("Logout parameter detected");
        }

        if (registered != null) {
            model.addAttribute("message", "Registration successful! Please login.");
            System.out.println("Registered parameter detected");
        }

        return "login";
    }
}