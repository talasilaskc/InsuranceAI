package com.project.InsureAI.dto;

import java.time.LocalDateTime;

public class ClaimDocumentResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;

    public ClaimDocumentResponse() {
    }

    public ClaimDocumentResponse(Long id, String fileName, String fileType, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
