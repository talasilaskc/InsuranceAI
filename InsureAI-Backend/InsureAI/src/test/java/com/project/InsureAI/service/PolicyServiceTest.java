package com.project.InsureAI.service;

import com.project.InsureAI.dto.PolicyIssuanceRequest;
import com.project.InsureAI.dto.PolicyRenewalRequest;
import com.project.InsureAI.dto.PolicyResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.project.InsureAI.entity.PolicyStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private AuthService authService;
    @Mock
    private AISystemRepository aiSystemRepository;
    @Mock
    private InsurancePolicyTypeRepository policyTypeRepository;
    @Mock
    private RiskAssessmentRepository riskRepository;
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PremiumCalculationEngine engine;
    @Mock
    private ClaimRepository claimRepository;

    @InjectMocks
    private PolicyService policyService;

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

    private AISystem aiSystem(Company c) {
        AISystem ai = new AISystem();
        ai.setId(5L);
        ai.setCompany(c);
        return ai;
    }

    private InsurancePolicyType policyType() {
        InsurancePolicyType t = new InsurancePolicyType();
        t.setId(2L);
        t.setActive(true);
        t.setBasePrice(10000);
        t.setMinCoverage(100000);
        t.setMaxCoverage(1000000);
        return t;
    }

    private RiskAssessment risk(AISystem ai) {
        RiskAssessment r = new RiskAssessment();
        r.setId(3L);
        r.setAiSystem(ai);
        r.setRiskLevel(RiskLevel.MEDIUM);
        r.setActive(true);
        return r;
    }

    @Test
    void issuePolicy_success() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();
        RiskAssessment risk = risk(ai);

        PolicyIssuanceRequest req = new PolicyIssuanceRequest();
        req.setAiSystemId(5L);
        req.setPolicyTypeId(2L);
        req.setCoverageLimit(500000);
        req.setDeductibleAmount(5000);
        req.setTenureYears(1);

        PremiumBreakdown breakdown = new PremiumBreakdown();
        breakdown.setTechnicalPremium(20000);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(policyTypeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.of(risk));
        when(policyRepository.findByAiSystemAndPolicyTypeAndIsActiveTrue(ai, type)).thenReturn(Optional.empty());
        when(engine.calculate(anyDouble(), anyDouble(), any(), anyInt(), anyDouble(), anyInt()))
                .thenReturn(breakdown);
        when(policyRepository.save(any())).thenAnswer(i -> {
            Policy p = i.getArgument(0);
            p.setId(100L);
            return p;
        });

        PolicyResponse res = policyService.issuePolicy(req);

        assertEquals(PENDING_APPROVAL, res.getStatus());
        verify(policyRepository).save(any());
    }

    @Test
    void renewPolicy_success() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();
        RiskAssessment risk = risk(ai);

        Policy old = new Policy();
        old.setId(1L);
        old.setAiSystem(ai);
        old.setPolicyType(type);
        old.setActive(true);

        PolicyRenewalRequest req = new PolicyRenewalRequest();
        req.setPolicyId(1L);
        req.setCoverageLimit(600000);
        req.setDeductibleAmount(5000);
        req.setTenureYears(1);

        PremiumBreakdown breakdown = new PremiumBreakdown();
        breakdown.setTechnicalPremium(25000);

        when(authService.getCurrentUser()).thenReturn(u);
        when(policyRepository.findById(1L)).thenReturn(Optional.of(old));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.of(risk));
        when(claimRepository.countByPolicyAndStatus(old, ClaimStatus.APPROVED)).thenReturn(1);
        when(policyRepository.findByAiSystemAndPolicyTypeAndIsActiveTrue(ai, type)).thenReturn(Optional.empty());
        when(engine.calculate(anyDouble(), anyDouble(), any(), anyInt(), anyDouble(), anyInt()))
                .thenReturn(breakdown);
        when(policyRepository.save(any())).thenAnswer(i -> {
            Policy p = i.getArgument(0);
            if (p.getId() == null) p.setId(200L);
            return p;
        });

        PolicyResponse res = policyService.renewPolicy(req);

        assertEquals(ACTIVE, res.getStatus());
        verify(policyRepository, times(2)).save(any());
    }
}