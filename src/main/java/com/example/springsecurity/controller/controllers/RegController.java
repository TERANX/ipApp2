package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.AppService;
import com.example.springsecurity.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class RegController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService service;

    @GetMapping("/reg")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) throws EmailExistsException {
        if (!StringUtils.hasText(userForm.getRoles())) {
            userForm.setRoles("Admin");
        }
//
//        if (bindingResult.hasErrors()) {
//            return "reg";
//        }

//        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            return "registration";
//        } // если сделаем проверку на правильность ввода пароля

//        if (!service.addUser(userForm)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "reg";
//        }
        userService.save(userForm);
        return "redirect:/login";
    }



}
