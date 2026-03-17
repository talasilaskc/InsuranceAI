package com.project.InsureAI.repository;

import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InsurancePolicyTypeRepository
        extends JpaRepository<InsurancePolicyType, Long> {

    List<InsurancePolicyType> findByIsActiveTrue();

    Optional<InsurancePolicyType> findById(Long id);

}