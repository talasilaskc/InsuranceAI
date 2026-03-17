package com.project.InsureAI.controller;

import com.project.InsureAI.dto.CreateRiskAssessmentRequest;
import com.project.InsureAI.dto.RiskAssessmentResponse;
import com.project.InsureAI.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*")
public class RiskAssessmentController {

    private final RiskAssessmentService riskService;

    public RiskAssessmentController(RiskAssessmentService riskService) {
        this.riskService = riskService;
    }

    @PostMapping
    public RiskAssessmentResponse createRisk(@RequestBody CreateRiskAssessmentRequest request) {
        return riskService.createRiskAssessment(request);
    }
}