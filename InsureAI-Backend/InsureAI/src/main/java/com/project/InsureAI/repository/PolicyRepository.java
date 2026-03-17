package com.project.InsureAI.repository;

import com.project.InsureAI.dto.PolicyRevenueDTO;
import com.project.InsureAI.dto.PolicySummaryDTO;
import com.project.InsureAI.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findByAiSystemAndPolicyTypeAndIsActiveTrue(
            AISystem aiSystem,
            InsurancePolicyType policyType
    );

    Policy findTopByAiSystemOrderByEndDateDesc(AISystem aiSystem);

    List<Policy> findByStatus(PolicyStatus status);

    List<Policy> findByAiSystem_Company(Company company);

    @Query("""
   SELECT new com.project.InsureAI.dto.PolicyRevenueDTO(
       p.policyType.name,
       COUNT(p),
       SUM(p.premiumAmount)
   )
   FROM Policy p
   WHERE p.status = 'APPROVED'
   GROUP BY p.policyType.name
""")
    List<PolicyRevenueDTO> getRevenueByPolicyType();

    long countByStatus(PolicyStatus status);

    long countByIsActiveTrue();

    @Query("""
   SELECT COUNT(p)
   FROM Policy p
   WHERE p.aiSystem.company.id = :companyId
   AND p.isActive = true
""")
    long countActivePoliciesForCompany(Long companyId);

    @Query("""
   SELECT COALESCE(SUM(p.remainingCoverage),0)
   FROM Policy p
   WHERE p.aiSystem.company.id = :companyId
   AND p.isActive = true
""")
    double sumRemainingCoverage(Long companyId);

    @Query("""
   SELECT new com.project.InsureAI.dto.PolicySummaryDTO(
        p.policyType.name,
        p.coverageLimit,
        p.remainingCoverage,
        p.status
   )
   FROM Policy p
   WHERE p.aiSystem.company.id = :companyId
   AND p.isActive = true
""")
    List<PolicySummaryDTO> getPolicySnapshot(Long companyId);

    List<Policy> findByUnderwriterId(Long underwriterId);

}