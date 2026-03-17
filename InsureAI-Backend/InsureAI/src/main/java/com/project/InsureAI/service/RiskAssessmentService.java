package com.project.InsureAI.service;

import com.project.InsureAI.dto.CreateRiskAssessmentRequest;
import com.project.InsureAI.dto.RiskAssessmentResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.AISystemRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RiskAssessmentService {

    private final RiskAssessmentRepository riskRepository;
    private final AISystemRepository aiSystemRepository;
    private final AuthService authService;
    private final RiskCalculationEngine riskEngine;

    public RiskAssessmentService(RiskAssessmentRepository riskRepository,
                                 AISystemRepository aiSystemRepository,
                                 AuthService authService,
                                 RiskCalculationEngine riskEngine) {
        this.riskRepository = riskRepository;
        this.aiSystemRepository = aiSystemRepository;
        this.authService = authService;
        this.riskEngine = riskEngine;
    }

    public RiskAssessmentResponse createRiskAssessment(CreateRiskAssessmentRequest request) {

        // Get logged-in company
        Company company = authService.getCurrentUser().getCompany();

        // Validate AI system belongs to this company
        AISystem aiSystem = aiSystemRepository
                .findByIdAndCompany(request.getAiSystemId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("AI System not found"));

        // Deactivate previous active risk (if exists)
        riskRepository.findByAiSystemAndIsActiveTrue(aiSystem)
                .ifPresent(existingRisk -> {
                    existingRisk.setActive(false);
                    riskRepository.save(existingRisk);
                });

        // 🧠 4️⃣ Create new RiskAssessment
        RiskAssessment risk = new RiskAssessment();

        risk.setAiSystem(aiSystem);

        risk.setHumanOversight(request.isHumanOversight());
        risk.setBiasTesting(request.isBiasTesting());
        risk.setAuditLogsMaintained(request.isAuditLogsMaintained());
        risk.setDataExposureCategory(request.getDataExposureCategory());
        risk.setLifeCriticalUsage(request.isLifeCriticalUsage());
        risk.setFinancialImpactLevel(request.getFinancialImpactLevel());
        risk.setPastIncidentCount(request.getPastIncidentCount());

        risk.setActive(true);
        risk.setAssessedAt(LocalDateTime.now());

        // 🧮 5️⃣ Calculate risk
        riskEngine.calculateRisk(risk);

        // 💾 6️⃣ Save
        RiskAssessment savedRisk = riskRepository.save(risk);

        // 📤 7️⃣ Map to Response DTO
        return new RiskAssessmentResponse(
                savedRisk.getId(),
                savedRisk.getAiSystem().getId(),
                savedRisk.getGovernanceScore(),
                savedRisk.getExposureScore(),
                savedRisk.getImpactScore(),
                savedRisk.getTotalRiskScore(),
                savedRisk.getRiskLevel(),
                savedRisk.isActive(),
                savedRisk.getAssessedAt()
        );
    }
}