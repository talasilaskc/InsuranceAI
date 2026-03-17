package com.project.InsureAI.repository;

import com.project.InsureAI.dto.AiRiskSnapshotDTO;
import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByAiSystemAndIsActiveTrue(AISystem aiSystem);

    @Query("""
   SELECT new com.project.InsureAI.dto.AiRiskSnapshotDTO(
        r.aiSystem.name,
        r.totalRiskScore,
        r.riskLevel
   )
   FROM RiskAssessment r
   WHERE r.aiSystem.company.id = :companyId
   AND r.isActive = true
""")
    List<AiRiskSnapshotDTO> getRiskSnapshot(Long companyId);
    Optional<RiskAssessment> findByPolicyIdAndIsActiveTrue(Long policyId);

    long countByUnderwriterAndAssessedAtAfter(MyUser underwriter, LocalDateTime date);

}