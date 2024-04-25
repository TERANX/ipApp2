package com.example.springsecurity.controller.controllers;

import com.example.springsecurity.model.Application;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.AppService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apps")
@AllArgsConstructor
public class AppController {

//    private AppService service;
//
//    @GetMapping("/welcome")
//    public  String welcome() {
//        return "Welcome to the unprotected page";
//    }
//
//    @GetMapping("/all-app")
//    @PreAuthorize("hasAuthority('ROLE_USER')") //аннотация описывает правило для получения доступа к данной контрольной точке
//    public List<Application> allApplication() {
//        return  service.allApplicatoions();
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public  Application applicationById(@PathVariable int id){
//        return service.applicationByID(id);
//    }
//
//    @PostMapping("/new-user")
//    public String addUser(@RequestBody User user){
//        service.addUser(user);
//        return "User is saved";
//    }

    // add realisation
//    @GetMapping("/")
//    public String getMainPage(){
//        return "index";
//    }
//
//    @GetMapping("/login")
//    public String getLogin(@RequestParam (value = "error", required = false) String error,
//                           @RequestParam (value = "logout", required = false) String logout,
//                           Model model){
//        model.addAttribute("error", error !=null);
//        model.addAttribute("logout", logout !=null);
//        return "login";
//    }

}
