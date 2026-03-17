package com.project.InsureAI.controller;

import com.project.InsureAI.dto.ClaimDocumentResponse;
import com.project.InsureAI.service.ClaimDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimDocumentController {

    private final ClaimDocumentService documentService;

    public ClaimDocumentController(ClaimDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/{claimId}/documents/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClaimDocumentResponse> uploadDocument(
            @PathVariable Long claimId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                documentService.uploadClaimDocument(claimId, file));
    }

    @GetMapping("/{claimId}/documents")
    public ResponseEntity<List<ClaimDocumentResponse>> getDocuments(
            @PathVariable Long claimId) {

        return ResponseEntity.ok(documentService.getDocuments(claimId));
    }

    @GetMapping("/documents/{docId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long docId) {

        Resource resource = documentService.downloadDocument(docId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}