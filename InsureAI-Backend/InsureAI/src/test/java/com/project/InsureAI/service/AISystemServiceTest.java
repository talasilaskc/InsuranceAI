package com.project.InsureAI.service;

import com.project.InsureAI.dto.CreateAISystemRequest;
import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.entity.DataExposureCategory;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.AISystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.project.InsureAI.entity.DeploymentType.CLOUD;
import static com.project.InsureAI.entity.ModelType.LLM;
import static com.project.InsureAI.entity.OwnershipType.OWNED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AISystemServiceTest {

    @Mock
    private AISystemRepository aiSystemRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AISystemService aiSystemService;

    private Company mockCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        return company;
    }

    private MyUser mockUser(Company company) {
        MyUser user = new MyUser();
        user.setId(10L);
        user.setCompany(company);
        return user;
    }

    @Test
    void createAISystem_success() {

        Company company = mockCompany();
        MyUser user = mockUser(company);

        CreateAISystemRequest request = new CreateAISystemRequest();
        request.setName("Fraud Detection AI");
        request.setDescription("Detects fraudulent claims");
        request.setOwnershipType(OWNED);
        request.setDeploymentType(CLOUD);
        request.setBusinessCritical(true);
        request.setVersion("v1");
        request.setModelType(LLM);
        request.setDataProcessedType(DataExposureCategory.GENERIC);
        request.setProductionSince(LocalDate.now());

        when(authService.getCurrentUser()).thenReturn(user);
        when(aiSystemRepository.save(any(AISystem.class)))
                .thenAnswer(invocation -> {
                    AISystem saved = invocation.getArgument(0);
                    saved.setId(100L);
                    return saved;
                });

        AISystem result = aiSystemService.createAISystem(request);

        assertNotNull(result);
        assertEquals("Fraud Detection AI", result.getName());
        assertEquals(company, result.getCompany());
        assertTrue(result.isActive());
        assertNotNull(result.getCreatedAt());

        verify(aiSystemRepository, times(1)).save(any(AISystem.class));
        verify(authService, times(1)).getCurrentUser();
    }

    @Test
    void getMyAISystems_success() {

        Company company = mockCompany();
        MyUser user = mockUser(company);

        AISystem ai1 = new AISystem();
        ai1.setId(1L);
        ai1.setName("AI 1");
        ai1.setCompany(company);

        AISystem ai2 = new AISystem();
        ai2.setId(2L);
        ai2.setName("AI 2");
        ai2.setCompany(company);

        when(authService.getCurrentUser()).thenReturn(user);
        when(aiSystemRepository.findByCompany(company))
                .thenReturn(List.of(ai1, ai2));

        List<AISystem> result = aiSystemService.getMyAISystems();

        assertEquals(2, result.size());
        verify(aiSystemRepository, times(1)).findByCompany(company);
    }

    @Test
    void getAISystemById_success() {

        Company company = mockCompany();
        MyUser user = mockUser(company);

        AISystem aiSystem = new AISystem();
        aiSystem.setId(5L);
        aiSystem.setName("Risk AI");
        aiSystem.setCompany(company);

        when(authService.getCurrentUser()).thenReturn(user);
        when(aiSystemRepository.findByIdAndCompany(5L, company))
                .thenReturn(Optional.of(aiSystem));

        AISystem result = aiSystemService.getAISystemById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(aiSystemRepository, times(1))
                .findByIdAndCompany(5L, company);
    }

    @Test
    void getAISystemById_notFound() {

        Company company = mockCompany();
        MyUser user = mockUser(company);

        when(authService.getCurrentUser()).thenReturn(user);
        when(aiSystemRepository.findByIdAndCompany(99L, company))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> aiSystemService.getAISystemById(99L));

        verify(aiSystemRepository, times(1))
                .findByIdAndCompany(99L, company);
    }
}