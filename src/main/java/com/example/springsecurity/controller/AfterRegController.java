package com.example.springsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AfterRegController {

    @GetMapping("/afterReg")
    public String afterRegistration(Model model, Authentication authentication) {
        System.out.println("=== AFTER REG / LOGIN PAGE ===");

        if (authentication != null && authentication.isAuthenticated()) {
            // Пользователь аутентифицирован
            String username = authentication.getName();
            System.out.println("Authenticated user: " + username);
            model.addAttribute("username", username);
        } else {
            System.out.println("User is not authenticated");
            // Если не аутентифицирован, перенаправляем на логин
            return "redirect:/custom-login";
        }

        return "OutMain"; // Возвращаем шаблон OutMain.html
    }
}