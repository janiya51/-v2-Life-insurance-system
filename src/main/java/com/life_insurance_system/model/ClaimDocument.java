package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ClaimDocuments")
public class ClaimDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private int documentId;

    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "document_type", length = 100)
    private String documentType;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "uploaded_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp uploadedAt;

    // Constructors
    public ClaimDocument() {
    }

    public ClaimDocument(Claim claim, String documentType, String filePath) {
        this.claim = claim;
        this.documentType = documentType;
        this.filePath = filePath;
    }

    // Getters and Setters
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}