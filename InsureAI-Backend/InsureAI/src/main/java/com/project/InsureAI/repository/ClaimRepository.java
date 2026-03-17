package com.project.InsureAI.repository;

import com.project.InsureAI.dto.ClaimTrendDTO;
import com.project.InsureAI.entity.Claim;
import com.project.InsureAI.entity.ClaimStatus;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    int countByPolicy(Policy policy);

    List<Claim> findByPolicy(Policy policy);

    int countByPolicyAndStatus(Policy policy, ClaimStatus status);

    List<Claim> findByPolicy_AiSystem_Company(Company company);

    long countByStatus(ClaimStatus status);

    List<Claim> findByStatus(ClaimStatus status);

    List<Claim> findByAssignedOfficer_Id(Long officerId);

    @Query("select coalesce(sum(c.payoutAmount),0) from Claim c where c.status='APPROVED'")
    double getTotalPayout();

    @Query("""
   SELECT new com.project.InsureAI.dto.ClaimTrendDTO(
       c.status,
       COUNT(c)
   )
   FROM Claim c
   GROUP BY c.status
""")
    List<ClaimTrendDTO> getClaimTrend();

    @Query("""
   SELECT COUNT(c)
   FROM Claim c
   WHERE c.policy.aiSystem.company.id = :companyId
""")
    long countClaimsForCompany(Long companyId);

    @Query("""
   SELECT COALESCE(MAX(c.payoutAmount),0)
   FROM Claim c
   WHERE c.policy.aiSystem.company.id = :companyId
   AND c.status = 'APPROVED'
""")
    double getLastPayout(Long companyId);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.company.id = :companyId")
    long countByCompanyId(Long companyId);



}