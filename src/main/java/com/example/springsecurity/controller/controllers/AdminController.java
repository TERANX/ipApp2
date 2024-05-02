package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.User;
//import com.example.springsecurity.model.dto.UserCreateDto;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository ur;

    @GetMapping({"/admin", "/"})
    public ModelAndView getAllEmployees() {
        ModelAndView mav = new ModelAndView("listUsers");
        mav.addObject("users", ur.findAll());
        return mav;
    }

    @GetMapping("/addUserForm")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("addUserForm");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user) {
        ur.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("addUserForm");
        User user = ur.findById(userId).get();
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        ur.deleteById(userId);
        return "redirect:/admin";
    }
}






//    @PostMapping("/admin")
//    public String deleteUser(Model model) {
//        User users = new User();
//        model.addAttribute("users", users);
//        User delete = userService.delete(users.getId());
//        return "admin";
//    }

//    @PostMapping("/save")
//    public String saveBooks(@ModelAttribute UserCreateDto form, Model model) {
//        userService.save((User) form.getUsers());
//
//        model.addAttribute("books", bookService.findAll());
//        return "redirect:/books/all";
//    }

//    @GetMapping("/admin/{id}")
//    public String gtUser(@PathVariable("id") Long userId, Model model) {
//        model.addAttribute("users", userService.usergtList(userId));
//        return "users";
//    }
//}