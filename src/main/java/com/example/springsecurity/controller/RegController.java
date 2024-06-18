package com.example.springsecurity.controller;

import com.example.springsecurity.errors.EmailExistsException;
import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.RoleEnum;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.RoleRepository;
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

import java.util.List;

@Controller
@AllArgsConstructor
public class RegController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;





    @GetMapping("/reg")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) throws EmailExistsException {

        Role role = new Role();
        role.setName(String.valueOf(RoleEnum.USER));
        User user = new User();
        user.setRoles((List<Role>) role);


        userService.save(userForm);
        return "redirect:/login";
    }



}
