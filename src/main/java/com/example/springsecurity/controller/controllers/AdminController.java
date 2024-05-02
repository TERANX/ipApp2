package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.User;
//import com.example.springsecurity.model.dto.UserCreateDto;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository ur;

//    @RequestMapping(value = "/showAll", method=RequestMethod.GET)
//    public String showAll() {
//        Set<User> users = (Set<User>) ur.findAll();
//
//       return "users";
//    }

//    @RequestMapping(value="/showteams", method=RequestMethod.GET)
//    public Set<Team> teams()
//    {
//        Set<Team> teams = (Set<Team>) teamRepository.findAll();
//        return teams;
//    }

    @GetMapping({"/admin", "/"})
    public ModelAndView getAllEmployees() {
        ModelAndView mav = new ModelAndView("listUsers");
        mav.addObject("users", ur.findAll());
        return mav;
    }

//    @GetMapping("/all")
//    public String showAll(Model model) {
//        model.addAttribute("users", userService.findAll());
//        return "users/allUsers";
//
//
//
//    }

//    @PostMapping("/delete")
//    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
//                              @RequestParam(required = true, defaultValue = "delete" ) String action,
//                              Model model) {
//        if (action.equals("delete")){
//            userService.delete(userId);
//        }
//        return "redirect:/admin";
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
}