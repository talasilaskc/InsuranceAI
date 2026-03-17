package com.project.InsureAI.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    private String country;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    private BigDecimal annualRevenue;

    private LocalDateTime createdAt;

    public Company() {
    }

    public Company(String name, String industry, String country,
                   CompanySize companySize, BigDecimal annualRevenue) {
        this.name = name;
        this.industry = industry;
        this.country = country;
        this.companySize = companySize;
        this.annualRevenue = annualRevenue;
        this.createdAt = LocalDateTime.now();
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getCountry() {
        return country;
    }

    public CompanySize getCompanySize() {
        return companySize;
    }

    public BigDecimal getAnnualRevenue() {
        return annualRevenue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCompanySize(CompanySize companySize) {
        this.companySize = companySize;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public void setId(Long id) {
        this.id = id;
    }
}