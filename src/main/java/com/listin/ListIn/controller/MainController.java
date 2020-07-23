package com.listin.ListIn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "";
    }

    @GetMapping("/checklists")
    public String checklists() {
        return "checklistSearch";
    }

    @GetMapping("/checklists/1")
    public String checklist() {
        return "checklist";
    }

    @GetMapping("/addItems")
    public String addItems() {
        return "items";
    }

    @GetMapping("/newItem")
    public String newItem() {
        return "newItem";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
