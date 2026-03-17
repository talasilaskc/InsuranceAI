package com.project.InsureAI.entity;

import jakarta.persistence.*;

@Entity
public class InsurancePolicyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;   // AI_DIRECT_LIABILITY / AI_API_DEPENDENCY

    private String description;

    private double basePrice;

    private boolean isActive;

    private double minCoverage;

    private double maxCoverage;

    public InsurancePolicyType() {}

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getMinCoverage() {
        return minCoverage;
    }

    public void setMinCoverage(double minCoverage) {
        this.minCoverage = minCoverage;
    }

    public double getMaxCoverage() {
        return maxCoverage;
    }

    public void setMaxCoverage(double maxCoverage) {
        this.maxCoverage = maxCoverage;
    }
}