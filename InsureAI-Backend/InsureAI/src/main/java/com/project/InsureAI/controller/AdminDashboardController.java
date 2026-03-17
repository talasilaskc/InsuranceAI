package com.project.InsureAI.controller;

import com.project.InsureAI.dto.AdminDashboardResponse;
import com.project.InsureAI.service.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping
    public AdminDashboardResponse getDashboard() {
        return adminDashboardService.getDashboardData();
    }
}