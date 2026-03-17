package com.project.InsureAI.controller;

import com.project.InsureAI.dto.LoginRequest;
import com.project.InsureAI.dto.LoginResponse;
import com.project.InsureAI.dto.RegisterRequest;
import com.project.InsureAI.dto.RegisterResponse;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        authService.registerCompany(request);

        return new RegisterResponse("Company registered successfully");
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        MyUser user = authService.getUserByEmail(request.getEmail());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(token, user.getRole().name());
    }

    @GetMapping("/test")
    public String test() {
        return "JWT WORKING!";
    }
}