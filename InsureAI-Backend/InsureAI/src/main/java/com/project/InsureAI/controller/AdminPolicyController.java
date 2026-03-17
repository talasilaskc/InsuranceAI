package com.project.InsureAI.controller;

import com.project.InsureAI.dto.PolicyResponse;
import com.project.InsureAI.entity.Policy;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/policies")
@CrossOrigin(origins = "*")
public class AdminPolicyController {
    private final PolicyRepository policyRepository;
    private final PolicyService policyService;

    public AdminPolicyController(PolicyRepository policyRepository, PolicyService policyService) {
        this.policyRepository = policyRepository;
        this.policyService = policyService;
    }

    @GetMapping("/pending")
    public List<Policy> getPending(){
        return policyRepository.findByStatus(PolicyStatus.PENDING_APPROVAL);
    }

    @GetMapping("/{id}")
    public Policy getById(@PathVariable Long id){
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    @GetMapping
    public List<Policy> getAll(){
        return policyService.getAllPolicies();
    }


}
