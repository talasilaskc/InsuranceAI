package com.project.InsureAI.service;

import com.project.InsureAI.dto.CreateUnderwriterRequest;
import com.project.InsureAI.dto.RiskAssessmentRequest;
import com.project.InsureAI.dto.UnderwriterResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.repository.MyUserRepository;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.InsureAI.entity.Role.ROLE_UNDERWRITER;

@Service
public class AdminService {
    private final PolicyRepository policyRepository;
    private final MyUserRepository userRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskCalculationEngine riskCalculationEngine;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AdminService(PolicyRepository policyRepository, MyUserRepository userRepository, RiskAssessmentRepository riskAssessmentRepository, RiskCalculationEngine riskCalculationEngine, PasswordEncoder passwordEncoder, AuthService authService) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskCalculationEngine = riskCalculationEngine;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public void createUnderwriter(CreateUnderwriterRequest request) {
        MyUser creator = authService.getCurrentUser();
        createUser(request.getFullName(), request.getEmail(), request.getPassword(), Role.ROLE_UNDERWRITER, creator.getCompany());
    }

    public void createClaimsOfficer(CreateUnderwriterRequest request) {
        MyUser creator = authService.getCurrentUser();
        createUser(request.getFullName(), request.getEmail(), request.getPassword(), Role.ROLE_CLAIMS_OFFICER, creator.getCompany());
    }

    private void createUser(String fullName, String email, String password, Role role, Company company) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        MyUser user = new MyUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setCompany(company);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void assignUnderwriter(Long policyId, Long underwriterId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        MyUser underwriter = userRepository.findById(underwriterId)
                .orElseThrow(() -> new RuntimeException("Underwriter not found"));

        if (!underwriter.getRole().equals(ROLE_UNDERWRITER)) {
            throw new RuntimeException("User is not an underwriter");
        }

        // if already risk assessed → reset workflow
        if (policy.getStatus() == PolicyStatus.RISK_ASSESSED ||
                policy.getStatus() == PolicyStatus.PENDING_APPROVAL) {

            policy.setStatus(PolicyStatus.UNDER_REVIEW);

            // deactivate previous assessment
            riskAssessmentRepository
                    .findByPolicyIdAndIsActiveTrue(policyId)
                    .ifPresent(ra -> {
                        ra.setActive(false);
                        riskAssessmentRepository.save(ra);
                    });
        }

        policy.setUnderwriter(underwriter);
        policy.setStatus(PolicyStatus.UNDER_REVIEW);
        policyRepository.save(policy);
    }

    public void submitRiskAssessment(Long policyId,
                                     RiskAssessmentRequest request,
                                     String email) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        MyUser underwriter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!policy.getUnderwriter().getId().equals(underwriter.getId())) {
            throw new RuntimeException("Not authorized");
        }

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

        // ⭐ USE YOUR ENGINE HERE
        riskCalculationEngine.calculateRisk(risk);

        risk.setActive(true);
        risk.setAssessedAt(LocalDateTime.now());

        riskAssessmentRepository.save(risk);

        policy.setStatus(PolicyStatus.RISK_ASSESSED);
        policyRepository.save(policy);

    }

    public List<Policy> getPendingApprovalPolicies() {

        return policyRepository.findByStatus(PolicyStatus.PENDING_APPROVAL);

    }

    public void overridePremium(Long policyId, double finalPremium) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getStatus() != PolicyStatus.PENDING_APPROVAL && 
            policy.getStatus() != PolicyStatus.RISK_ASSESSED) {
            throw new RuntimeException("Premium can be edited only in assessment stage. (Current status: " + policy.getStatus() + ")");
        }

        policy.setPremiumAmount(finalPremium);

        policyRepository.save(policy);

    }

    public void approvePolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getStatus() != PolicyStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Policy not ready for approval");
        }

        policy.setStatus(PolicyStatus.APPROVED);
        policy.setActive(false);
        policy.setRemainingCoverage(policy.getCoverageLimit());

        policyRepository.save(policy);

    }

    public void rejectPolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getStatus() != PolicyStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Policy not ready for rejection");
        }

        policy.setStatus(PolicyStatus.REJECTED);
        policy.setActive(false);

        policyRepository.save(policy);

    }

    public List<UnderwriterResponse> getAllUnderwriters() {

        List<MyUser> underwriters =
                userRepository.findByRoleAndIsActiveTrue(Role.ROLE_UNDERWRITER);

        return underwriters.stream()
                .map(u -> new UnderwriterResponse(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail()
                ))
                .toList();
    }






}
