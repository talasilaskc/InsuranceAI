package com.project.InsureAI.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ClaimDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    private String fileName;
    private String fileType;

    // store only stored filename (not full path)
    private String storedFileName;

    private LocalDateTime uploadedAt;

    public ClaimDocument() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Claim getClaim() { return claim; }
    public void setClaim(Claim claim) { this.claim = claim; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getStoredFileName() { return storedFileName; }
    public void setStoredFileName(String storedFileName) { this.storedFileName = storedFileName; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}