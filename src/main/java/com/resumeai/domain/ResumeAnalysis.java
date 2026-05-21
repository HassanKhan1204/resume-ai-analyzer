package com.resumeai.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_document_id", nullable = false)
    private ResumeDocument resumeDocument;

    @ElementCollection
    @CollectionTable(name = "resume_missing_skills", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill", nullable = false)
    private List<String> missingSkills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "resume_suggestions", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "suggestion", nullable = false, length = 1000)
    private List<String> suggestions = new ArrayList<>();

    @Column(nullable = false)
    private int jobMatchingScore;

    @Column(nullable = false, columnDefinition = "text")
    private String summary;

    @Column(nullable = false)
    private String analyzer;

    @Column(nullable = false)
    private Instant analyzedAt = Instant.now();

    protected ResumeAnalysis() {
    }

    public ResumeAnalysis(
            ResumeDocument resumeDocument,
            List<String> missingSkills,
            List<String> suggestions,
            int jobMatchingScore,
            String summary,
            String analyzer
    ) {
        this.resumeDocument = resumeDocument;
        this.missingSkills = new ArrayList<>(missingSkills);
        this.suggestions = new ArrayList<>(suggestions);
        this.jobMatchingScore = jobMatchingScore;
        this.summary = summary;
        this.analyzer = analyzer;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public int getJobMatchingScore() {
        return jobMatchingScore;
    }

    public String getSummary() {
        return summary;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public Instant getAnalyzedAt() {
        return analyzedAt;
    }
}
