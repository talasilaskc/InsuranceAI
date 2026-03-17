package com.project.InsureAI.dto;

import com.project.InsureAI.entity.*;

import java.time.LocalDate;

public class CreateAISystemRequest {

    private String name;
    private String description;

    private OwnershipType ownershipType;
    private DeploymentType deploymentType;
    private boolean businessCritical;

    private String version;
    private ModelType modelType;
    private DataExposureCategory dataExposureCategory;

    private LocalDate productionSince;

    public CreateAISystemRequest() {
    }

    public CreateAISystemRequest(String name, String description, OwnershipType ownershipType, DeploymentType deploymentType, boolean businessCritical, String version, ModelType modelType, DataExposureCategory dataExposureCategory, LocalDate productionSince) {
        this.name = name;
        this.description = description;
        this.ownershipType = ownershipType;
        this.deploymentType = deploymentType;
        this.businessCritical = businessCritical;
        this.version = version;
        this.modelType = modelType;
        this.dataExposureCategory = dataExposureCategory;
        this.productionSince = productionSince;
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

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public void setDeploymentType(DeploymentType deploymentType) {
        this.deploymentType = deploymentType;
    }

    public boolean isBusinessCritical() {
        return businessCritical;
    }

    public void setBusinessCritical(boolean businessCritical) {
        this.businessCritical = businessCritical;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public DataExposureCategory getDataProcessedType() {
        return dataExposureCategory;
    }

    public void setDataProcessedType(DataExposureCategory dataExposureCategory) {
        this.dataExposureCategory = dataExposureCategory;
    }

    public LocalDate getProductionSince() {
        return productionSince;
    }

    public void setProductionSince(LocalDate productionSince) {
        this.productionSince = productionSince;
    }
}