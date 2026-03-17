package com.project.InsureAI.controller;

import com.project.InsureAI.dto.AdminDecisionRequest;
import com.project.InsureAI.dto.AssignOfficerResponse;
import com.project.InsureAI.service.AdminClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/claims")
public class AdminClaimController {

    private final AdminClaimService adminClaimService;

    public AdminClaimController(AdminClaimService adminClaimService) {
        this.adminClaimService = adminClaimService;
    }

    @GetMapping("/officers")
    public ResponseEntity<java.util.List<com.project.InsureAI.entity.MyUser>> getOfficers() {
        return ResponseEntity.ok(adminClaimService.getClaimsOfficers());
    }

    @PutMapping("/{id}/mark-under-review")
    public ResponseEntity<String> markUnderReview(@PathVariable Long id) {
        adminClaimService.markUnderReview(id);
        return ResponseEntity.ok("Claim marked as under admin review");
    }

    @PutMapping("/{id}/assign/{officerId}")
    public ResponseEntity<AssignOfficerResponse> assignOfficer(@PathVariable Long id, @PathVariable Long officerId) {
        return ResponseEntity.ok(adminClaimService.assignOfficer(id, officerId));
    }

    @PutMapping("/{id}/final-approve")
    public ResponseEntity<String> finalApprove(@PathVariable Long id, @RequestBody AdminDecisionRequest request) {
        adminClaimService.finalApprove(id, request.getPayoutAmount(), request.getRemarks());
        return ResponseEntity.ok("Claim approved and payment is in progress");
    }

    @PutMapping("/{id}/final-reject")
    public ResponseEntity<String> finalReject(@PathVariable Long id, @RequestBody AdminDecisionRequest request) {
        adminClaimService.finalReject(id, request.getRemarks());
        return ResponseEntity.ok("Claim rejected");
    }

    @PutMapping("/{id}/mark-settled")
    public ResponseEntity<String> markSettled(@PathVariable Long id) {
        adminClaimService.markSettled(id);
        return ResponseEntity.ok("Claim marked as settled");
    }
}
