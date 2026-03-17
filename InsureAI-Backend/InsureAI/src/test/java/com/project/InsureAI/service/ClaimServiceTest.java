package com.project.InsureAI.service;

import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.dto.SubmitClaimRequest;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.project.InsureAI.entity.PolicyStatus.ACTIVE;
import static com.project.InsureAI.entity.PolicyStatus.EXPIRED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ClaimService claimService;

    private Company company() {
        Company c = new Company();
        c.setId(1L);
        return c;
    }

    private MyUser user(Company c) {
        MyUser u = new MyUser();
        u.setId(10L);
        u.setCompany(c);
        return u;
    }

    private Policy activePolicy(Company c) {
        AISystem ai = new AISystem();
        ai.setCompany(c);

        Policy p = new Policy();
        p.setId(5L);
        p.setAiSystem(ai);
        p.setStatus(ACTIVE);
        p.setActive(true);
        p.setRemainingCoverage(100000);
        p.setDeductibleAmount(1000);
        return p;
    }

    @Test
    void submitClaim_success() {

        Company c = company();
        MyUser u = user(c);
        Policy policy = activePolicy(c);

        SubmitClaimRequest req = new SubmitClaimRequest();
        req.setPolicyId(5L);
        req.setClaimAmount(5000);

        Claim saved = new Claim();
        saved.setId(100L);
        saved.setPolicy(policy);
        saved.setClaimAmount(5000);
        saved.setClaimDate(LocalDate.now());
        saved.setStatus(ClaimStatus.RAISED);
        saved.setPayoutAmount(0);
        saved.setVerifiedLoss(0);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(5L)).thenReturn(Optional.of(policy));
        when(claimRepository.save(any())).thenReturn(saved);

        ClaimResponse res = claimService.submitClaim(req);

        assertNotNull(res);
        assertEquals(ClaimStatus.RAISED, res.getStatus());
        verify(claimRepository, times(1)).save(any());
    }

    @Test
    void submitClaim_policyNotFound() {

        Company c = company();
        MyUser u = user(c);

        SubmitClaimRequest req = new SubmitClaimRequest();
        req.setPolicyId(99L);
        req.setClaimAmount(2000);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> claimService.submitClaim(req));
    }

    @Test
    void submitClaim_unauthorizedCompany() {

        Company logged = company();
        Company other = new Company();
        other.setId(2L);

        MyUser u = user(logged);
        Policy policy = activePolicy(other);

        SubmitClaimRequest req = new SubmitClaimRequest();
        req.setPolicyId(5L);
        req.setClaimAmount(2000);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(5L)).thenReturn(Optional.of(policy));

        assertThrows(BusinessException.class,
                () -> claimService.submitClaim(req));
    }

    @Test
    void submitClaim_policyNotActive() {

        Company c = company();
        MyUser u = user(c);
        Policy policy = activePolicy(c);
        policy.setActive(false);

        SubmitClaimRequest req = new SubmitClaimRequest();
        req.setPolicyId(5L);
        req.setClaimAmount(2000);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(5L)).thenReturn(Optional.of(policy));

        assertThrows(BusinessException.class,
                () -> claimService.submitClaim(req));
    }

    @Test
    void approveClaim_success() {

        Company c = company();
        Policy policy = activePolicy(c);

        Claim claim = new Claim();
        claim.setId(10L);
        claim.setStatus(ClaimStatus.RAISED);
        claim.setPolicy(policy);
        claim.setClaimAmount(10000);
        claim.setClaimDate(LocalDate.now());

        when(claimRepository.findById(10L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ClaimResponse res = claimService.approveClaim(10L, 8000);

        assertEquals(ClaimStatus.APPROVED, res.getStatus());
        verify(policyRepository, times(1)).save(policy);
        verify(claimRepository, times(1)).save(claim);
    }

    @Test
    void approveClaim_notFound() {

        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> claimService.approveClaim(1L, 5000));
    }

    @Test
    void approveClaim_alreadyProcessed() {

        Policy policy = activePolicy(company());
        Claim claim = new Claim();
        claim.setId(10L);
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.APPROVED);

        when(claimRepository.findById(10L)).thenReturn(Optional.of(claim));

        assertThrows(ConflictException.class,
                () -> claimService.approveClaim(10L, 5000));
    }

    @Test
    void approveClaim_exceedsCoverage() {

        Company c = company();
        Policy policy = activePolicy(c);

        policy.setRemainingCoverage(500);     // VERY SMALL
        policy.setDeductibleAmount(0);        // IMPORTANT

        Claim claim = new Claim();
        claim.setId(10L);
        claim.setStatus(ClaimStatus.RAISED);
        claim.setPolicy(policy);

        when(claimRepository.findById(10L)).thenReturn(Optional.of(claim));

        assertThrows(ConflictException.class,
                () -> claimService.approveClaim(10L, 10000));

        assertEquals(EXPIRED, policy.getStatus());
        assertFalse(policy.isActive());

        verify(policyRepository, never()).save(any());
        verify(claimRepository, never()).save(any());
    }

    @Test
    void rejectClaim_success() {

        Policy policy = activePolicy(company());

        Claim claim = new Claim();
        claim.setId(5L);
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.RAISED);
        claim.setClaimAmount(4000);
        claim.setClaimDate(LocalDate.now());

        when(claimRepository.findById(5L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ClaimResponse res = claimService.rejectClaim(5L);

        assertEquals(ClaimStatus.REJECTED, res.getStatus());
        verify(claimRepository).save(claim);
    }

    @Test
    void getClaimsForPolicy_success() {

        Company c = company();
        MyUser u = user(c);
        Policy policy = activePolicy(c);

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setPolicy(policy);
        claim.setClaimAmount(2000);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus(ClaimStatus.RAISED);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(5L)).thenReturn(Optional.of(policy));
        when(claimRepository.findByPolicy(policy)).thenReturn(List.of(claim));

        List<ClaimResponse> list = claimService.getClaimsForPolicy(5L);

        assertEquals(1, list.size());
    }

    @Test
    void getClaimsForCompany_success() {

        Company c = company();
        MyUser u = user(c);

        Policy policy = activePolicy(c);

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.RAISED);
        claim.setClaimAmount(1000);
        claim.setClaimDate(LocalDate.now());

        when(authService.getCurrentUser()).thenReturn(u);
        when(claimRepository.findByPolicy_AiSystem_Company(c))
                .thenReturn(List.of(claim));

        List<ClaimResponse> res = claimService.getClaimsForCompany();

        assertEquals(1, res.size());
    }

    @Test
    void getAllClaimsForAdmin_success() {

        Policy policy = activePolicy(company());

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.RAISED);
        claim.setClaimAmount(1000);
        claim.setClaimDate(LocalDate.now());

        when(claimRepository.findAll()).thenReturn(List.of(claim));

        List<ClaimResponse> res = claimService.getAllClaimsForAdmin();

        assertEquals(1, res.size());
        verify(claimRepository).findAll();
    }
}