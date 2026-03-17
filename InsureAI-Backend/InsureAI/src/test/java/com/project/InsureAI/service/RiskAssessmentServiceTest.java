package com.project.InsureAI.service;

import com.project.InsureAI.dto.CreateRiskAssessmentRequest;
import com.project.InsureAI.dto.RiskAssessmentResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.AISystemRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskAssessmentServiceTest {

    @Mock
    private RiskAssessmentRepository riskRepository;

    @Mock
    private AISystemRepository aiSystemRepository;

    @Mock
    private AuthService authService;

    @Mock
    private RiskCalculationEngine riskEngine;

    @InjectMocks
    private RiskAssessmentService service;

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

    @Test
    void createRiskAssessment_success_withoutPreviousRisk() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);

        CreateRiskAssessmentRequest req = new CreateRiskAssessmentRequest();
        req.setAiSystemId(5L);
        req.setHumanOversight(true);
        req.setBiasTesting(true);
        req.setAuditLogsMaintained(true);
        req.setLifeCriticalUsage(false);
        req.setPastIncidentCount(1);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            RiskAssessment r = invocation.getArgument(0);
            r.setGovernanceScore(70);
            r.setExposureScore(60);
            r.setImpactScore(65);
            r.setTotalRiskScore(65);
            r.setRiskLevel(RiskLevel.MEDIUM);
            return null;
        }).when(riskEngine).calculateRisk(any());

        when(riskRepository.save(any())).thenAnswer(i -> {
            RiskAssessment r = i.getArgument(0);
            r.setId(100L);
            return r;
        });

        RiskAssessmentResponse res = service.createRiskAssessment(req);

        assertEquals(100L, res.getId());
        assertEquals(RiskLevel.MEDIUM, res.getRiskLevel());
        verify(riskEngine).calculateRisk(any());
        verify(riskRepository).save(any());
    }

    @Test
    void createRiskAssessment_success_withPreviousRiskDeactivated() {

        Company c = company();
        MyUser u = user(c);
        AISystem ai = aiSystem(c);

        RiskAssessment existing = new RiskAssessment();
        existing.setId(50L);
        existing.setAiSystem(ai);
        existing.setActive(true);

        CreateRiskAssessmentRequest req = new CreateRiskAssessmentRequest();
        req.setAiSystemId(5L);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.of(ai));
        when(riskRepository.findByAiSystemAndIsActiveTrue(ai)).thenReturn(Optional.of(existing));

        doNothing().when(riskEngine).calculateRisk(any());

        when(riskRepository.save(any())).thenAnswer(i -> {
            RiskAssessment r = i.getArgument(0);
            if (r.getId() == null) r.setId(200L);
            return r;
        });

        RiskAssessmentResponse res = service.createRiskAssessment(req);

        assertFalse(existing.isActive());
        verify(riskRepository, times(2)).save(any());
        assertEquals(200L, res.getId());
    }

    @Test
    void createRiskAssessment_aiSystemNotFound() {

        Company c = company();
        MyUser u = user(c);

        CreateRiskAssessmentRequest req = new CreateRiskAssessmentRequest();
        req.setAiSystemId(5L);

        when(authService.getCurrentUser()).thenReturn(u);
        when(aiSystemRepository.findByIdAndCompany(5L, c)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createRiskAssessment(req));
    }
}