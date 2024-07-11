package com.doubles.selfstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @PostMapping("/login")
    public void login() {
        // login post
    }

    @PostMapping("/regist")
    public void regist() {
        // regist post
    }

    @GetMapping("/profile")
    public void profile() {
        // profile get
    }
}
