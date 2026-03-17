package com.project.InsureAI.controller;

import com.project.InsureAI.entity.Policy;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.service.PolicyService;
import com.project.InsureAI.service.UnderwriterService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.InsureAI.dto.RiskAssessmentRequest;

import java.util.List;

@RestController
@RequestMapping("/api/underwriter")
public class UnderwriterController {
    private final UnderwriterService underwriterService;
    private final PolicyService policyService;
    private final PolicyRepository policyRepository;
    public UnderwriterController(UnderwriterService underwriterService,PolicyService policyService,PolicyRepository policyRepository) {
        this.underwriterService = underwriterService;
        this.policyService=policyService;
        this.policyRepository=policyRepository;
    }

    @GetMapping("/my-policies")
    public ResponseEntity<List<Policy>> getMyPolicies(Authentication authentication) {
        String email = authentication.getName();
        List<Policy> policies = underwriterService.getAssignedPolicies(email);

        return ResponseEntity.ok(policies);
    }

    @GetMapping("/policies/{id}")
    public Policy getById(@PathVariable Long id){
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    @PostMapping("/policies/{id}/risk")
    public ResponseEntity<?> submitRisk(@PathVariable Long id,
                                       @RequestBody RiskAssessmentRequest request,
                                       Authentication authentication) {
        underwriterService.submitRiskAssessment(id, request, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getStats(Authentication authentication) {
        return ResponseEntity.ok(underwriterService.getDashboardStats(authentication.getName()));
    }

}
