package com.project.InsureAI.repository;

import com.project.InsureAI.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    long count();
}
