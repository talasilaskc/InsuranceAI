package com.project.InsureAI.service;

import com.project.InsureAI.dto.RiskAssessmentRequest;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.Policy;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.entity.RiskAssessment;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.MyUserRepository;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnderwriterService {
    private final MyUserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskCalculationEngine riskCalculationEngine;
    private final PremiumCalculationEngine premiumCalculationEngine;
    private final ClaimRepository claimRepository;

    public UnderwriterService(MyUserRepository userRepository, PolicyRepository policyRepository, RiskAssessmentRepository riskAssessmentRepository, RiskCalculationEngine riskCalculationEngine, PremiumCalculationEngine premiumCalculationEngine, ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskCalculationEngine = riskCalculationEngine;
        this.premiumCalculationEngine = premiumCalculationEngine;
        this.claimRepository = claimRepository;
    }

    public List<Policy> getAssignedPolicies(String email) {

        MyUser underwriter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return policyRepository.findByUnderwriterId(underwriter.getId());

    }

    public void submitRiskAssessment(Long policyId,
                                     RiskAssessmentRequest request,
                                     String email) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        MyUser underwriter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (policy.getUnderwriter() == null ||
                !policy.getUnderwriter().getId().equals(underwriter.getId())) {
            throw new RuntimeException("Unauthorized policy access");
        }

// ⭐ Create new risk assessment snapshot
        RiskAssessment risk = new RiskAssessment();
        risk.setPolicy(policy);
        risk.setUnderwriter(underwriter);

        risk.setHumanOversight(request.isHumanOversight());
        risk.setBiasTesting(request.isBiasTesting());
        risk.setAuditLogsMaintained(request.isAuditLogsMaintained());
        risk.setDataExposureCategory(request.getDataExposureCategory());
        risk.setLifeCriticalUsage(request.isLifeCriticalUsage());
        risk.setFinancialImpactLevel(request.getFinancialImpactLevel());
        risk.setPastIncidentCount(request.getPastIncidentCount());
        risk.setRemarks(request.getRemarks());

// ⭐ Calculate risk score
        riskCalculationEngine.calculateRisk(risk);

        risk.setActive(true);
        risk.setAssessedAt(LocalDateTime.now());

        riskAssessmentRepository.save(risk);

// ⭐ Freeze risk snapshot in policy
        policy.setRiskSnapshot(risk);

// ⭐ Fetch previous policy claim experience
        Policy previousPolicy =
                policyRepository.findTopByAiSystemOrderByEndDateDesc(policy.getAiSystem());

        int claimCountPreviousTerm = 0;

        if (previousPolicy != null) {
            claimCountPreviousTerm =
                    claimRepository.countByPolicy(previousPolicy);
        }

// ⭐ Calculate FINAL premium after underwriting
        PremiumBreakdown pb = premiumCalculationEngine.calculate(
                policy.getPolicyType().getBasePrice(),
                policy.getCoverageLimit(),
                risk.getRiskLevel(),
                claimCountPreviousTerm,
                policy.getDeductibleAmount(),
                policy.getTenureYears()
        );

        policy.setPremiumAmount(pb.totalPayable);

// ⭐ Move workflow to admin decision stage
        policy.setStatus(PolicyStatus.PENDING_APPROVAL);
        policyRepository.save(policy);


    }
    public Map<String, Object> getDashboardStats(String email) {
        MyUser underwriter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Policy> assigned = policyRepository.findByUnderwriterId(underwriter.getId());

        long assignedTasks = assigned.stream()
                .filter(p -> p.getStatus() == PolicyStatus.UNDER_REVIEW)
                .count();

        long pendingReviews = assignedTasks;

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        long completedToday = riskAssessmentRepository.countByUnderwriterAndAssessedAtAfter(underwriter, startOfDay);

        Map<String, Object> stats = new HashMap<>();
        stats.put("assignedTasks", assignedTasks);
        stats.put("pendingReviews", pendingReviews);
        stats.put("completedToday", completedToday);
        return stats;
    }
}
