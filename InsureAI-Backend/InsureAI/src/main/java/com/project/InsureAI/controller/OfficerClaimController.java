package com.project.InsureAI.controller;

import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.dto.OfficerRecommendationRequest;
import com.project.InsureAI.service.ClaimOfficerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officer/claims")
public class OfficerClaimController {

    private final ClaimOfficerService officerService;

    public OfficerClaimController(ClaimOfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<ClaimResponse>> getAssignedClaims() {
        return ResponseEntity.ok(officerService.getAssignedClaims());
    }

    @PutMapping("/{id}/start-investigation")
    public ResponseEntity<String> startInvestigation(@PathVariable Long id) {
        officerService.startInvestigation(id);
        return ResponseEntity.ok("Investigation started");
    }

    @PutMapping("/{id}/recommend")
    public ResponseEntity<String> recommendPayout(@PathVariable Long id, @RequestBody OfficerRecommendationRequest request) {
        officerService.recommendPayout(id, request);
        return ResponseEntity.ok("Payout recommendation submitted");
    }
}
