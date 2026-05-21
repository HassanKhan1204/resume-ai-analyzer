package com.resumeai.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.UUID;

@Entity
public class ResumeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false, columnDefinition = "text")
    private String extractedText;

    @Column(nullable = false)
    private Instant uploadedAt = Instant.now();

    @OneToOne(mappedBy = "resumeDocument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ResumeAnalysis analysis;

    protected ResumeDocument() {
    }

    public ResumeDocument(String originalFilename, String contentType, long sizeBytes, String storagePath, String extractedText) {
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storagePath = storagePath;
        this.extractedText = extractedText;
    }

    public UUID getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public ResumeAnalysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(ResumeAnalysis analysis) {
        this.analysis = analysis;
    }
}
