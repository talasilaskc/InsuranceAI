package com.project.InsureAI.service;

import com.project.InsureAI.dto.PolicyRenewalRequest;
import com.project.InsureAI.dto.PolicyIssuanceRequest;
import com.project.InsureAI.dto.PolicyResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.project.InsureAI.entity.PolicyStatus.*;

@Service
public class PolicyService {

    private final AuthService authService;
    private final AISystemRepository aiSystemRepository;
    private final InsurancePolicyTypeRepository policyTypeRepository;
    private final RiskAssessmentRepository riskRepository;
    private final PolicyRepository policyRepository;
    private final PremiumCalculationEngine engine;
    private final ClaimRepository claimRepository;

    public PolicyService(AuthService authService,
                         AISystemRepository aiSystemRepository,
                         InsurancePolicyTypeRepository policyTypeRepository,
                         RiskAssessmentRepository riskRepository,
                         PolicyRepository policyRepository,
                         PremiumCalculationEngine engine,
                         ClaimRepository claimRepository) {

        this.authService = authService;
        this.aiSystemRepository = aiSystemRepository;
        this.policyTypeRepository = policyTypeRepository;
        this.riskRepository = riskRepository;
        this.policyRepository = policyRepository;
        this.engine = engine;
        this.claimRepository = claimRepository;
    }

    @Transactional
    public List<Policy> getAllPolicies() {
        List<Policy> policies = policyRepository.findAll();
        return policies;
    }

    public List<PolicyResponse> getCompanyPolicies() {

        Company company = authService.getCurrentUser().getCompany();

        List<Policy> policies =
                policyRepository.findByAiSystem_Company(company);

        return policies.stream().map(p -> new PolicyResponse(
                p.getId(),
                p.getAiSystem().getId(),
                p.getAiSystem().getName(),
                p.getPolicyType().getId(),
                p.getCoverageLimit(),
                p.getDeductibleAmount(),
                p.getPremiumAmount(),
                p.getStartDate(),
                p.getEndDate(),
                p.getStatus()
        )).toList();
    }

    @Transactional
    public PolicyResponse issuePolicy(PolicyIssuanceRequest request) {

        Company company = authService.getCurrentUser().getCompany();

        AISystem aiSystem = aiSystemRepository
                .findByIdAndCompany(request.getAiSystemId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("AI System not found"));

        InsurancePolicyType policyType = policyTypeRepository
                .findById(request.getPolicyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        if (!policyType.isActive()) {
            throw new BusinessException("Policy type inactive");
        }

        if (request.getCoverageLimit() < policyType.getMinCoverage()
                || request.getCoverageLimit() > policyType.getMaxCoverage()) {
            throw new BusinessException("Coverage outside allowed limits");
        }

        if (request.getDeductibleAmount() >= request.getCoverageLimit()) {
            throw new BusinessException("Deductible must be less than coverage");
        }

        // ⭐ prevent duplicate active / pipeline policy
        policyRepository
                .findByAiSystemAndPolicyTypeAndIsActiveTrue(aiSystem, policyType)
                .ifPresent(p -> {
                    throw new ConflictException("Active policy already exists");
                });

        Policy policy = new Policy();

        policy.setAiSystem(aiSystem);
        policy.setPolicyType(policyType);

        policy.setCoverageLimit(request.getCoverageLimit());
        policy.setRemainingCoverage(request.getCoverageLimit());

        policy.setDeductibleAmount(request.getDeductibleAmount());
        policy.setTenureYears(request.getTenureYears());

        policy.setStatus(PolicyStatus.APPLIED);
        policy.setActive(false);

        Policy saved = policyRepository.save(policy);

        return mapToResponse(saved);
    }


    @Transactional
    public Policy approvePolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (policy.getStatus() != PENDING_APPROVAL) {
            throw new ConflictException("Policy already processed");
        }

        policy.setStatus(APPROVED);
        policy.setActive(false);

        return policyRepository.save(policy);
    }

    @Transactional
    public Policy activatePolicy(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (policy.getStatus() != APPROVED) {
            throw new BusinessException("Payment only allowed for approved policies");
        }

        policy.setStatus(ACTIVE);
        policy.setActive(true);
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(policy.getTenureYears()));

        return policyRepository.save(policy);
    }

    @Transactional
    public Policy cancelPayment(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (policy.getStatus() != APPROVED) {
            throw new BusinessException("Action only allowed for approved policies waiting for payment");
        }

        policy.setStatus(REJECTED);
        policy.setActive(false);

        return policyRepository.save(policy);
    }

    @Transactional
    public Policy rejectPolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (policy.getStatus() != PENDING_APPROVAL) {
            throw new ConflictException("Policy already processed");
        }

        policy.setStatus(REJECTED);
        policy.setActive(false);

        return policyRepository.save(policy);
    }



    @Transactional
    public PolicyResponse renewPolicy(PolicyRenewalRequest request) {

        Company company = authService.getCurrentUser().getCompany();

        Policy oldPolicy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (!oldPolicy.getAiSystem().getCompany().getId()
                .equals(company.getId())) {
            throw new BusinessException("Unauthorized renewal attempt");
        }

        if (oldPolicy.getStatus() != PolicyStatus.ACTIVE &&
                oldPolicy.getStatus() != PolicyStatus.EXPIRED) {
            throw new BusinessException("Only ACTIVE or EXPIRED policies can be renewed");
        }

        oldPolicy.setActive(false);
        policyRepository.save(oldPolicy);

        Policy newPolicy = new Policy();

        newPolicy.setAiSystem(oldPolicy.getAiSystem());
        newPolicy.setPolicyType(oldPolicy.getPolicyType());

        newPolicy.setCoverageLimit(request.getCoverageLimit());
        newPolicy.setRemainingCoverage(request.getCoverageLimit());

        newPolicy.setDeductibleAmount(request.getDeductibleAmount());
        newPolicy.setTenureYears(request.getTenureYears());

        newPolicy.setRenewedFromPolicyId(oldPolicy.getId());

        newPolicy.setStatus(PolicyStatus.APPLIED);
        newPolicy.setActive(false);

        Policy saved = policyRepository.save(newPolicy);

        return mapToResponse(saved);
    }



    // ⭐ COMMON MAPPER
    public PolicyResponse mapToResponse(Policy p) {
        return new PolicyResponse(
                p.getId(),
                p.getAiSystem().getId(),
                p.getAiSystem().getName(),
                p.getPolicyType().getId(),
                p.getCoverageLimit(),
                p.getDeductibleAmount(),
                p.getPremiumAmount(),
                p.getStartDate(),
                p.getEndDate(),
                p.getStatus()
        );
    }

}