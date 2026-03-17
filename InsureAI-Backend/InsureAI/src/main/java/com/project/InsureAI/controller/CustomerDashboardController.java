package com.project.InsureAI.controller;

import com.project.InsureAI.dto.CustomerDashboardResponse;
import com.project.InsureAI.service.CustomerDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/dashboard")
@CrossOrigin(origins = "*")
public class CustomerDashboardController {

    private final CustomerDashboardService dashboardService;

    public CustomerDashboardController(CustomerDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public CustomerDashboardResponse getDashboard() {
        return dashboardService.getDashboardData();
    }
}