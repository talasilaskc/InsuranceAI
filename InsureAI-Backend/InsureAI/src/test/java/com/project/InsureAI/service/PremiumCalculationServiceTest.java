package com.project.InsureAI.service;

import com.project.InsureAI.dto.PremiumCalculationRequest;
import com.project.InsureAI.dto.PremiumCalculationResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremiumCalculationServiceTest {

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
    private ClaimRepository claimRepository;
    @Mock
    private PremiumCalculationEngine engine;

    @InjectMocks
    private PremiumCalculationService service;

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
    void calculatePremium_success() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();
        RiskAssessment risk = risk(ai);

        PremiumCalculationRequest req = new PremiumCalculationRequest();
        req.setAiSystemId(5L);
        req.setPolicyTypeId(2L);
        req.setCoverageLimit(500000);
        req.setDeductibleAmount(5000);
        req.setTenureYears(2);

        Policy prev = new Policy();
        prev.setId(9L);

        PremiumBreakdown breakdown = new PremiumBreakdown();
        breakdown.setBasePrice(10000);
        breakdown.setExposurePremium(15000);
        breakdown.setRiskAdjusted(17000);
        breakdown.setExperienceAdjusted(18000);
        breakdown.setDeductibleAdjusted(17500);
        breakdown.setTechnicalPremium(20000);
        breakdown.setTax(3600);
        breakdown.setTotalPayable(23600);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(policyTypeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.of(risk));
        when(policyRepository.findTopByAiSystemOrderByEndDateDesc(ai)).thenReturn(prev);
        when(claimRepository.countByPolicy(prev)).thenReturn(2);
        when(engine.calculate(anyDouble(), anyDouble(), any(), anyInt(), anyDouble(), anyInt()))
                .thenReturn(breakdown);

        PremiumCalculationResponse res = service.calculatePremium(req);

        assertEquals(23600, res.getTotalPayable());
        verify(engine).calculate(anyDouble(), anyDouble(), any(), anyInt(), anyDouble(), anyInt());
    }

    @Test
    void calculatePremium_aiSystemNotFound() {

        Company c = company();
        MyUser u = user(c);

        PremiumCalculationRequest req = new PremiumCalculationRequest();
        req.setAiSystemId(5L);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.calculatePremium(req));
    }

    @Test
    void calculatePremium_policyTypeInactive() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();
        type.setActive(false);

        PremiumCalculationRequest req = new PremiumCalculationRequest();
        req.setAiSystemId(5L);
        req.setPolicyTypeId(2L);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(policyTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        assertThrows(BusinessException.class,
                () -> service.calculatePremium(req));
    }

    @Test
    void calculatePremium_invalidCoverage() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();

        PremiumCalculationRequest req = new PremiumCalculationRequest();
        req.setAiSystemId(5L);
        req.setPolicyTypeId(2L);
        req.setCoverageLimit(50000); // below min

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(policyTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        assertThrows(BusinessException.class,
                () -> service.calculatePremium(req));
    }

    @Test
    void calculatePremium_noRiskAssessment() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);
        InsurancePolicyType type = policyType();

        PremiumCalculationRequest req = new PremiumCalculationRequest();
        req.setAiSystemId(5L);
        req.setPolicyTypeId(2L);
        req.setCoverageLimit(500000);
        req.setDeductibleAmount(5000);
        req.setTenureYears(2);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(policyTypeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.calculatePremium(req));
    }
}