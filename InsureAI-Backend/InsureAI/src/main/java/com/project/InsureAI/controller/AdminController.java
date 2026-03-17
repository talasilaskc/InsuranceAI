package com.project.InsureAI.controller;

import com.project.InsureAI.dto.CreateUnderwriterRequest;
import com.project.InsureAI.dto.PremiumOverrideRequest;
import com.project.InsureAI.dto.UnderwriterResponse;
import com.project.InsureAI.entity.Policy;
import com.project.InsureAI.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final AdminService adminService;
    AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @PostMapping("/create-underwriter")
    public ResponseEntity<String> createUnderwriter(
            @RequestBody CreateUnderwriterRequest request) {
        adminService.createUnderwriter(request);
        return ResponseEntity.ok("Underwriter created successfully");
    }

    @PostMapping("/create-claims-officer")
    public ResponseEntity<String> createClaimsOfficer(
            @RequestBody CreateUnderwriterRequest request) {
        adminService.createClaimsOfficer(request);
        return ResponseEntity.ok("Claims Officer created successfully");
    }

    @PutMapping("/assign-underwriter/{policyId}/{underwriterId}")
    public ResponseEntity<String> assignUnderwriter(
            @PathVariable Long policyId,
            @PathVariable Long underwriterId) {
        System.out.println("Assigning underwriter with ID " + underwriterId + " to policy with ID " + policyId);
        adminService.assignUnderwriter(policyId, underwriterId);
        return ResponseEntity.ok("Underwriter assigned successfully");
    }

    @GetMapping("/policies/pending-approval")
    public ResponseEntity<List<Policy>> getPendingApprovalPolicies() {


        List<Policy> policies = adminService.getPendingApprovalPolicies();
        return ResponseEntity.ok(policies);

    }

    @PutMapping("/policies/{policyId}/override-premium")
    public ResponseEntity<?> overridePremium(
            @PathVariable Long policyId,
            @RequestBody PremiumOverrideRequest request) {
        adminService.overridePremium(policyId, request.getPremiumAmount());
        return ResponseEntity.ok(Collections.singletonMap("message", "Premium updated successfully"));
    }

    @PutMapping("/policies/{policyId}/approve")
    public ResponseEntity<?> approvePolicy(@PathVariable Long policyId) {
        adminService.approvePolicy(policyId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Policy approved successfully"));
    }

    @PutMapping("/policies/{policyId}/reject")
    public ResponseEntity<?> rejectPolicy(@PathVariable Long policyId) {
        adminService.rejectPolicy(policyId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Policy rejected"));
    }

    @GetMapping("/underwriters")
    public List<UnderwriterResponse> getAllUnderwriters() {
        return adminService.getAllUnderwriters();
    }
}
