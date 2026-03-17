package com.project.InsureAI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AISystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private OwnershipType ownershipType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private DeploymentType deploymentType;

    private boolean businessCritical;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String version;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ModelType modelType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private DataExposureCategory dataExposureCategory;

    private LocalDate productionSince;

    private boolean isActive;

    private LocalDateTime createdAt;

    public AISystem() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public Long setId(Long id) {
        this.id = id;
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public boolean isBusinessCritical() {
        return businessCritical;
    }

    public Company getCompany() {
        return company;
    }

    public String getVersion() {
        return version;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public DataExposureCategory getDataProcessedType() {
        return dataExposureCategory;
    }

    public LocalDate getProductionSince() {
        return productionSince;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public void setDeploymentType(DeploymentType deploymentType) {
        this.deploymentType = deploymentType;
    }

    public void setBusinessCritical(boolean businessCritical) {
        this.businessCritical = businessCritical;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public void setDataProcessedType(DataExposureCategory dataExposureCategory) {
        this.dataExposureCategory = dataExposureCategory;
    }

    public void setProductionSince(LocalDate productionSince) {
        this.productionSince = productionSince;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}