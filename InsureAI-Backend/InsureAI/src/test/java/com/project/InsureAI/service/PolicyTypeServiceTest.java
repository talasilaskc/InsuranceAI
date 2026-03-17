package com.project.InsureAI.service;

import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.InsurancePolicyTypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PolicyTypeServiceTest {

    @Mock
    InsurancePolicyTypeRepository repository;

    @InjectMocks
    PolicyTypeService service;

    @Test
    void testCreatePolicyType() {

        InsurancePolicyType input = new InsurancePolicyType();
        input.setName("AI Liability");

        when(repository.save(input)).thenReturn(input);

        InsurancePolicyType result = service.createPolicyType(input);

        assertTrue(result.isActive());
        assertEquals("AI Liability", result.getName());
    }

    @Test
    void testUpdatePolicyTypeSuccess() {

        InsurancePolicyType existing = new InsurancePolicyType();
        existing.setId(1L);
        existing.setName("Old");

        InsurancePolicyType updated = new InsurancePolicyType();
        updated.setName("New");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        InsurancePolicyType result =
                service.updatePolicyType(1L, updated);

        assertEquals("New", result.getName());
    }

    @Test
    void testUpdatePolicyTypeNotFound() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updatePolicyType(99L, new InsurancePolicyType()));
    }

    @Test
    void testGetAllPolicyTypes() {

        when(repository.findAll()).thenReturn(List.of(new InsurancePolicyType()));

        List<InsurancePolicyType> list = service.getAllPolicyTypes();

        assertEquals(1, list.size());
    }
}