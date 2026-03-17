package com.project.InsureAI.controller;

import com.project.InsureAI.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin
public class TestMailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/mail")
    public String sendTestMail(@RequestParam String email) {

        emailService.sendTestMail(email);

        return "Test email sent successfully";
    }
}
