package com.project.InsureAI.controller;

import com.project.InsureAI.dto.SubmitClaimRequest;
import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.service.ClaimService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/submit")
    public ClaimResponse submitClaim(@RequestBody SubmitClaimRequest request) {
        return claimService.submitClaim(request);
    }

    @GetMapping("/policy/{policyId}")
    public List<ClaimResponse> getClaimsForPolicy(@PathVariable Long policyId) {
        return claimService.getClaimsForPolicy(policyId);
    }

    @GetMapping("/company")
    public List<ClaimResponse> getClaimsForCompany(){
        return claimService.getClaimsForCompany();
    }

    @GetMapping("/{id}")
    public ClaimResponse getClaim(@PathVariable Long id) {
        return claimService.getClaimById(id);
    }

    @GetMapping("/admin/all")
    public List<ClaimResponse> getAllClaimsForAdmin() {
        return claimService.getAllClaimsForAdmin();
    }
}
