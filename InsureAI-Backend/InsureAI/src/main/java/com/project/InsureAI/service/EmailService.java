package com.project.InsureAI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestMail(String toEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("InsureAI Email Service Test");
        message.setText(
                "Hello,\n\n" +
                "This is a test email from InsureAI backend.\n" +
                "If you received this, SMTP configuration is working correctly.\n\n" +
                "Regards,\nInsureAI System"
        );

        mailSender.send(message);
    }
}
