package com.project.InsureAI.service;

import com.project.InsureAI.dto.PremiumCalculationRequest;
import com.project.InsureAI.dto.PremiumCalculationResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PremiumCalculationService {

    private final AuthService authService;
    private final AISystemRepository aiSystemRepository;
    private final InsurancePolicyTypeRepository policyTypeRepository;
    private final RiskAssessmentRepository riskRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final PremiumCalculationEngine engine;

    public PremiumCalculationService(AuthService authService,
                                     AISystemRepository aiSystemRepository,
                                     InsurancePolicyTypeRepository policyTypeRepository,
                                     RiskAssessmentRepository riskRepository,
                                     PolicyRepository policyRepository,
                                     ClaimRepository claimRepository,
                                     PremiumCalculationEngine engine) {
        this.authService = authService;
        this.aiSystemRepository = aiSystemRepository;
        this.policyTypeRepository = policyTypeRepository;
        this.riskRepository = riskRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
        this.engine = engine;
    }

    public PremiumCalculationResponse calculatePremium(PremiumCalculationRequest request) {

        // 🔐 1️⃣ Get Logged-in Company
        Company company = authService.getCurrentUser().getCompany();

        // 🔐 2️⃣ Validate AI system belongs to company
        AISystem aiSystem = aiSystemRepository
                .findByIdAndCompany(request.getAiSystemId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("AI System not found"));

        // 📄 3️⃣ Fetch Policy Type
        InsurancePolicyType policyType = policyTypeRepository
                .findById(request.getPolicyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        if (!policyType.isActive()) {
            throw new BusinessException("Policy Type is inactive");
        }

        // 📏 4️⃣ Validate Coverage Bounds
        if (request.getCoverageLimit() < policyType.getMinCoverage() ||
                request.getCoverageLimit() > policyType.getMaxCoverage()) {

            throw new BusinessException("Coverage outside allowed limits");
        }

        // 📏 5️⃣ Validate Deductible
        if (request.getDeductibleAmount() >= request.getCoverageLimit()) {
            throw new BusinessException("Deductible cannot exceed coverage");
        }

        if (request.getTenureYears() < 1 || request.getTenureYears() > 3) {
            throw new BusinessException("Invalid tenure selection");
        }

        // 📊 6️⃣ Fetch Active Risk
        RiskAssessment activeRisk = riskRepository
                .findByAiSystemAndIsActiveTrue(aiSystem)
                .orElseThrow(() -> new BusinessException("No active risk assessment found"));

        // 🔁 7️⃣ Fetch Previous Term Policy (if exists)
        Policy previousPolicy =
                policyRepository.findTopByAiSystemOrderByEndDateDesc(aiSystem);

        int claimCountPreviousTerm = 0;

        if (previousPolicy != null) {
            claimCountPreviousTerm =
                    claimRepository.countByPolicy(previousPolicy);
        }

        // 🧮 8️⃣ Call Pricing Engine
        PremiumBreakdown breakdown = engine.calculate(
                policyType.getBasePrice(),
                request.getCoverageLimit(),
                activeRisk.getRiskLevel(),
                claimCountPreviousTerm,
                request.getDeductibleAmount(),
                request.getTenureYears()
        );

        // 📤 9️⃣ Map to Response DTO
        return new PremiumCalculationResponse(
                breakdown.basePrice,
                breakdown.exposurePremium,
                breakdown.riskAdjusted,
                breakdown.experienceAdjusted,
                breakdown.deductibleAdjusted,
                breakdown.technicalPremium,
                breakdown.tax,
                breakdown.totalPayable
        );
    }
}