package com.project.InsureAI.controller;

import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final AuthService authService;

    public TestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/test-user")
    public String getCurrentUser() {

        MyUser user = authService.getCurrentUser();

        return "Logged in user: " + user.getEmail() +
                " | Company: " + user.getCompany().getName();
    }
}
