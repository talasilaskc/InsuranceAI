package com.project.InsureAI.service;

import com.project.InsureAI.dto.AssignOfficerResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.MyUserRepository;
import com.project.InsureAI.repository.PolicyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.project.InsureAI.entity.PolicyStatus.EXPIRED;

@Service
public class AdminClaimService {

    private final ClaimRepository claimRepository;
    private final MyUserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final AuthService authService;

    public AdminClaimService(ClaimRepository claimRepository,
                             MyUserRepository userRepository,
                             PolicyRepository policyRepository,
                             AuthService authService) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.authService = authService;
    }

    private void validateSameCompany(Claim claim, MyUser adminUser) {
        if (adminUser == null) return;

        // 1. Global Admin Check: If role is ROLE_ADMIN and no company is assigned, they are global.
        if (adminUser.getRole() == Role.ROLE_ADMIN && adminUser.getCompany() == null) {
            return;
        }

        // 2. If reaching here and company is null, they shouldn't even have access to this module
        if (adminUser.getCompany() == null) {
            throw new BusinessException("User has no associated company for validation");
        }

        // 3. Extract IDs safely
        Long adminCompanyId = adminUser.getCompany().getId();
        
        if (claim.getPolicy() == null || 
            claim.getPolicy().getAiSystem() == null || 
            claim.getPolicy().getAiSystem().getCompany() == null) {
            // Data integrity issue or global resource, allow global admin but restrict local
            return; 
        }

        Long claimCompanyId = claim.getPolicy().getAiSystem().getCompany().getId();

        if (!claimCompanyId.equals(adminCompanyId)) {
            throw new BusinessException("Unauthorized cross-company claim access");
        }
    }

    public java.util.List<MyUser> getClaimsOfficers() {
        MyUser admin = authService.getCurrentUser();
        return userRepository.findByRoleAndIsActiveTrue(Role.ROLE_CLAIMS_OFFICER).stream()
                .filter(u -> {
                    // Global admin sees all officers
                    if (admin.getRole() == Role.ROLE_ADMIN && admin.getCompany() == null) return true;
                    // Otherwise match by company
                    return admin.getCompany() != null && u.getCompany() != null &&
                            u.getCompany().getId().equals(admin.getCompany().getId());
                })
                .toList();
    }

    @Transactional
    public void markUnderReview(Long claimId) {

        MyUser admin = authService.getCurrentUser();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateSameCompany(claim, admin);

        if (claim.getStatus() != ClaimStatus.CLAIM_RAISED) {
            throw new ConflictException("Only RAISED claims can be moved to Admin Review");
        }

        claim.setStatus(ClaimStatus.UNDER_ADMIN_REVIEW);
        claimRepository.save(claim);
    }

    @Transactional
    public AssignOfficerResponse assignOfficer(Long claimId, Long officerId) {

        MyUser admin = authService.getCurrentUser();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateSameCompany(claim, admin);

        if (claim.getStatus() != ClaimStatus.UNDER_ADMIN_REVIEW) {
            throw new ConflictException("Claim must be in UNDER_ADMIN_REVIEW to assign officer");
        }

        MyUser officer = userRepository.findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Officer not found"));

        if (officer.getRole() != Role.ROLE_CLAIMS_OFFICER) {
            throw new BusinessException("Selected user is not a Claims Officer");
        }

        if (admin.getRole() == Role.ROLE_ADMIN && admin.getCompany() == null) {
            // Global admin can assign any officer
        } else if (admin.getCompany() == null || officer.getCompany() == null ||
                !officer.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new BusinessException("Officer belongs to different company coverage scope");
        }

        claim.setAssignedOfficer(officer);
        claim.setStatus(ClaimStatus.ASSIGNED_TO_OFFICER);

        Claim saved = claimRepository.save(claim);

        return new AssignOfficerResponse(
                saved.getId(),
                officer.getId(),
                officer.getFullName(),
                saved.getStatus().name()
        );
    }


    @Transactional
    public void finalApprove(Long claimId, double finalPayout, String remarks) {

        MyUser admin = authService.getCurrentUser();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateSameCompany(claim, admin);

        if (claim.getStatus() != ClaimStatus.OFFICER_RECOMMENDED && 
            claim.getStatus() != ClaimStatus.UNDER_ADMIN_REVIEW) {
            throw new ConflictException("Claim not ready for final approval");
        }

        if (finalPayout < 0) {
            throw new BusinessException("Final payout cannot be negative");
        }

        double officerSuggested = claim.getRecommendedPayoutAmount();

        if (officerSuggested > 0 && finalPayout > officerSuggested * 1.5) {
            throw new BusinessException("Admin payout unusually higher than officer recommendation");
        }

        Policy policy = claim.getPolicy();

        double deductible = policy.getDeductibleAmount();
        double adjustedPayout = finalPayout - deductible;

        if (adjustedPayout < 0) {
            adjustedPayout = 0;
        }

        if (adjustedPayout > policy.getRemainingCoverage()) {
            throw new BusinessException("Payout ₹" + adjustedPayout + " exceeds remaining coverage ₹" + policy.getRemainingCoverage());
        }

        // Update Policy Coverage
        policy.setRemainingCoverage(policy.getRemainingCoverage() - adjustedPayout);

        if (policy.getRemainingCoverage() <= 0) {
            policy.setStatus(EXPIRED);
            policy.setActive(false);
        }

        policyRepository.save(policy);

        // Update Claim Record
        claim.setVerifiedLoss(finalPayout);
        claim.setPayoutAmount(adjustedPayout);
        claim.setAdminRemarks(remarks);
        claim.setStatus(ClaimStatus.SETTLED); // Automatically set to SETTLED on approval per request
        claim.setApprovalDate(LocalDateTime.now());

        claimRepository.save(claim);
    }

    @Transactional
    public void finalReject(Long claimId, String remarks) {

        MyUser admin = authService.getCurrentUser();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateSameCompany(claim, admin);

        if (claim.getStatus() != ClaimStatus.OFFICER_RECOMMENDED &&
                claim.getStatus() != ClaimStatus.UNDER_ADMIN_REVIEW) {
            throw new ConflictException("Claim cannot be rejected in current state");
        }

        claim.setStatus(ClaimStatus.REJECTED);
        claim.setAdminRemarks(remarks);

        claimRepository.save(claim);
    }

    @Transactional
    public void markSettled(Long claimId) {

        MyUser admin = authService.getCurrentUser();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateSameCompany(claim, admin);

        if (claim.getStatus() != ClaimStatus.PAYMENT_IN_PROGRESS) {
            throw new ConflictException("Only claims in PAYMENT_IN_PROGRESS can be marked as SETTLED");
        }

        claim.setStatus(ClaimStatus.SETTLED);
        claimRepository.save(claim);
    }
}