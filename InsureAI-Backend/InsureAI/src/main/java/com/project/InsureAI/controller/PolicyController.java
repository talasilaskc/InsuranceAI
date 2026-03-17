package com.project.InsureAI.controller;

import com.project.InsureAI.dto.PolicyIssuanceRequest;
import com.project.InsureAI.dto.PolicyRenewalRequest;
import com.project.InsureAI.dto.PolicyResponse;
import com.project.InsureAI.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/issue")
    public PolicyResponse issuePolicy(
            @RequestBody PolicyIssuanceRequest request) {

        return policyService.issuePolicy(request);
    }

    @PostMapping("/renew")
    public PolicyResponse renewPolicy(
            @RequestBody PolicyRenewalRequest request) {

        return policyService.renewPolicy(request);
    }

    @GetMapping("/company")
    public List<PolicyResponse> getAllPolicy() {
        return policyService.getCompanyPolicies();
    }

    @PutMapping("/{id}/pay")
    public PolicyResponse activatePolicy(@PathVariable Long id) {
        return policyService.mapToResponse(policyService.activatePolicy(id));
    }

    @PutMapping("/{id}/cancel-payment")
    public PolicyResponse cancelPayment(@PathVariable Long id) {
        return policyService.mapToResponse(policyService.cancelPayment(id));
    }
}