package com.example.springsecurity.controller.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ControllerForm {
    // Без thymeleaf будет выводится стандартная форма секьюрити, не будет срабатывать индекс.штмл,
    // при подключенной аннотации RestController не срабатывает индекс.штмл , но при этом если сделать  вложенный класс
//        public class AppController {
    // то  он отрабатывает


    // add realisation "/"
    @GetMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "Home Page");
        return "index";
    }

    @GetMapping("/afterReg")
    public String afterReg(Model model) {
        model.addAttribute("title", "Home Page");
        return "afterReg";
    }

    @GetMapping("/afterLoginAdmin")
    public String afterRegAdmin(Model model) {
        model.addAttribute("title", "Home Page");
        return "afterLoginAdmin";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam("error" ) final Optional<String> error,
                           @RequestParam("logout") final Optional<String> logout,
                           Model model) {

        error.ifPresent( e ->  model.addAttribute("error", "Неправильный логин или пароль")
            );


        logout.ifPresent( e -> model.addAttribute("logout", e));

        return "login";
    }

    @PostMapping("/login")
    public String toHome(Model model, @RequestParam("email") String email, @RequestParam("password") String password){
        model.addAttribute("title", "Home Page");
        System.out.println(email);
        System.out.println(password);
            return "redirect:/index";
//            return "redirect:/afterReg";

        }


}
