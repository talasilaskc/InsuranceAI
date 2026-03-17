package com.project.InsureAI.controller;

import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.service.PolicyTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/policy-types")
@CrossOrigin(origins = "*")
public class AdminPolicyTypeController {

    private final PolicyTypeService service;

    public AdminPolicyTypeController(PolicyTypeService service) {
        this.service = service;
    }

    @PostMapping
    public InsurancePolicyType create(@RequestBody InsurancePolicyType type) {
        return service.createPolicyType(type);
    }

    @PutMapping("/{id}")
    public InsurancePolicyType update(@PathVariable Long id,
                                      @RequestBody InsurancePolicyType updated) {
        return service.updatePolicyType(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        service.deactivatePolicyType(id);
    }

    @GetMapping
    public List<InsurancePolicyType> getAll() {
        return service.getAllPolicyTypes();
    }
}