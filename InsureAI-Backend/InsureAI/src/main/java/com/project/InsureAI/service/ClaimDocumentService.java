package com.project.InsureAI.service;

import com.project.InsureAI.dto.ClaimDocumentResponse;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.ClaimDocumentRepository;
import com.project.InsureAI.repository.ClaimRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimDocumentService {

    private final ClaimDocumentRepository documentRepository;
    private final ClaimRepository claimRepository;
    private final LocalFileStorageService storageService;
    private final AuthService authService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public ClaimDocumentService(
            ClaimDocumentRepository documentRepository,
            ClaimRepository claimRepository,
            LocalFileStorageService storageService,
            AuthService authService) {

        this.documentRepository = documentRepository;
        this.claimRepository = claimRepository;
        this.storageService = storageService;
        this.authService = authService;
    }

    public ClaimDocumentResponse uploadClaimDocument(Long claimId,
                                                     MultipartFile file) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        MyUser user = authService.getCurrentUser();
        validateAccess(claim, user);

        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("File exceeds 10MB limit");
        }

        String name = file.getOriginalFilename().toLowerCase();

        if (!(name.endsWith(".pdf") ||
                name.endsWith(".jpg") ||
                name.endsWith(".jpeg") ||
                name.endsWith(".png"))) {

            throw new BusinessException("Invalid file type");
        }

        String storedName = storageService.storeFile(claimId, file);

        ClaimDocument doc = new ClaimDocument();
        doc.setClaim(claim);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setStoredFileName(storedName);
        doc.setUploadedAt(LocalDateTime.now());

        ClaimDocument saved = documentRepository.save(doc);

        return new ClaimDocumentResponse(
                saved.getId(),
                saved.getFileName(),
                saved.getFileType(),
                saved.getUploadedAt()
        );
    }

    public List<ClaimDocumentResponse> getDocuments(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        validateAccess(claim, authService.getCurrentUser());

        return documentRepository.findByClaim_Id(claimId)
                .stream()
                .map(d -> new ClaimDocumentResponse(
                        d.getId(),
                        d.getFileName(),
                        d.getFileType(),
                        d.getUploadedAt()))
                .toList();
    }

    public Resource downloadDocument(Long docId) {

        ClaimDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        validateAccess(doc.getClaim(), authService.getCurrentUser());

        return storageService.loadFile(doc.getStoredFileName());
    }

    private void validateAccess(Claim claim, MyUser user) {

        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        boolean isOwner =
                user.getCompany() != null &&
                        claim.getPolicy().getAiSystem()
                                .getCompany().getId()
                                .equals(user.getCompany().getId());

        boolean isOfficer =
                user.getRole() == Role.ROLE_CLAIMS_OFFICER &&
                        claim.getAssignedOfficer() != null &&
                        claim.getAssignedOfficer().getId().equals(user.getId());

        if (!isAdmin && !isOwner && !isOfficer) {
            throw new BusinessException("Unauthorized document access");
        }
    }
}