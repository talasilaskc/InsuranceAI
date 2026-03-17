package com.project.InsureAI.service;

import com.project.InsureAI.dto.CreateAISystemRequest;
import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.AISystemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AISystemService {

    private final AISystemRepository aiSystemRepository;
    private final AuthService authService;

    public AISystemService(AISystemRepository aiSystemRepository,
                           AuthService authService) {
        this.aiSystemRepository = aiSystemRepository;
        this.authService = authService;
    }

    public AISystem createAISystem(CreateAISystemRequest request) {

        Company company = authService.getCurrentUser().getCompany();

        AISystem aiSystem = new AISystem();
        aiSystem.setName(request.getName());
        aiSystem.setDescription(request.getDescription());
        aiSystem.setOwnershipType(request.getOwnershipType());
        aiSystem.setDeploymentType(request.getDeploymentType());
        aiSystem.setBusinessCritical(request.isBusinessCritical());
        aiSystem.setVersion(request.getVersion());
        aiSystem.setModelType(request.getModelType());
        aiSystem.setDataProcessedType(request.getDataProcessedType());
        aiSystem.setProductionSince(request.getProductionSince());
        aiSystem.setCompany(company);
        aiSystem.setActive(true);
        aiSystem.setCreatedAt(LocalDateTime.now());

        return aiSystemRepository.save(aiSystem);
    }

    public List<AISystem> getMyAISystems() {
        Company company = authService.getCurrentUser().getCompany();
        return aiSystemRepository.findByCompany(company);
    }

    public AISystem getAISystemById(Long id) {
        Company company = authService.getCurrentUser().getCompany();
        return aiSystemRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("AI System not found"));
    }
}