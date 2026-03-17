package com.project.InsureAI.repository;

import com.project.InsureAI.entity.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Long> {
    List<ClaimDocument> findByClaim_Id(Long claimId);
}
