package com.project.InsureAI.controller;

import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.repository.InsurancePolicyTypeRepository;
import com.project.InsureAI.service.PolicyTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy-types")
@CrossOrigin(origins = "*")
public class PolicyTypeController {

    private final PolicyTypeService service;

    public PolicyTypeController(PolicyTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<InsurancePolicyType> getActivePolicyTypes() {
        return service.getActivePolicyTypes();
    }

}