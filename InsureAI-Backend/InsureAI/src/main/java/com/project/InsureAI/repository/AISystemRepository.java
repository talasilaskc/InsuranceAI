package com.project.InsureAI.repository;

import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AISystemRepository extends JpaRepository<AISystem, Long> {

    List<AISystem> findByCompany(Company company);

    Optional<AISystem> findByIdAndCompany(Long id, Company company);

    long countByCompanyId(Long companyId);
}