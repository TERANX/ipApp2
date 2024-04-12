package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.Application;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.AppService;
import com.example.springsecurity.service.UserService;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.FormSubmitEvent;
import java.util.List;
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ControllerForm {
    // Без thymeleaf будет выводится стандартная форма секьюрити, не будет срабатывать индекс.штмл,
    // при подключенной аннотации RestController не срабатывает индекс.штмл , но при этом если сделать  вложенный класс
//        public class AppController {
    // то  он отрабатывает

    private UserService userService;

    private AppService service;

        // add realisation "/"
        @GetMapping("/")
        public String home(Model model){
            model.addAttribute("title", "Home Page");
            return "index";
        }

        @GetMapping("/login")
        public String getLogin(@RequestParam (value = "error", required = false) String error,
                               @RequestParam (value = "logout", required = false) String logout,
                               Model model){
            model.addAttribute("error", error !=null);
            model.addAttribute("logout", logout !=null);
            return "login";
        }

    @GetMapping("/reg")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("user") @Valid User user, Model model){
        userService.save(user);
//        @JsonSubTypes.Type(FormSubmitEvent.class)
        service.addUser(user);
//        return "redirect:/login";
        return "reg";
    }

//    }

}
