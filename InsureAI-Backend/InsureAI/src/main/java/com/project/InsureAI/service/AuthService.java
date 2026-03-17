package com.project.InsureAI.service;

import com.project.InsureAI.dto.RegisterRequest;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.Role;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.repository.CompanyRepository;
import com.project.InsureAI.repository.MyUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MyUserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MyUserRepository userRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCompany(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("User already exists");
        }

        Company company = new Company(
                request.getCompanyName(),
                request.getIndustry(),
                request.getCountry(),
                request.getCompanySize(),
                request.getAnnualRevenue()
        );

        companyRepository.save(company);

        MyUser user = new MyUser(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.ROLE_COMPANY,
                company
        );

        userRepository.save(user);
    }

    public MyUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User account not found"));
    }

    public MyUser getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("User not authenticated");
        }

        String email = authentication.getName();

        return getUserByEmail(email);
    }
}