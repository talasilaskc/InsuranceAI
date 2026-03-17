package com.project.InsureAI.service;

import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.InsurancePolicyTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyTypeService {

    private final InsurancePolicyTypeRepository repository;

    public PolicyTypeService(InsurancePolicyTypeRepository repository) {
        this.repository = repository;
    }

    // CREATE POLICY TYPE
    public InsurancePolicyType createPolicyType(InsurancePolicyType type) {
        type.setActive(true);
        return repository.save(type);
    }

    // UPDATE POLICY TYPE
    public InsurancePolicyType updatePolicyType(Long id, InsurancePolicyType updated) {

        InsurancePolicyType type = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        type.setName(updated.getName());
        type.setDescription(updated.getDescription());
        type.setBasePrice(updated.getBasePrice());
        type.setActive(updated.isActive());
        type.setMaxCoverage(updated.getMaxCoverage());
        type.setMinCoverage(updated.getMinCoverage());

        return repository.save(type);
    }

    // SOFT DELETE (Deactivate)
    public void deactivatePolicyType(Long id) {

        InsurancePolicyType type = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        type.setActive(false);
        repository.save(type);
    }

    // GET ALL
    public List<InsurancePolicyType> getAllPolicyTypes() {
        return repository.findAll();
    }

    public List<InsurancePolicyType> getActivePolicyTypes() {
        return repository.findByIsActiveTrue();
    }



}