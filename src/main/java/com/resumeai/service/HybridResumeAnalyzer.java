package com.resumeai.service;

import com.resumeai.config.LlmProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

@Primary
@Service
public class HybridResumeAnalyzer implements ResumeAnalyzer {

    private final LlmProperties properties;
    private final LlmClient llmClient;
    private final HeuristicResumeAnalyzer fallbackAnalyzer;

    public HybridResumeAnalyzer(LlmProperties properties, LlmClient llmClient, HeuristicResumeAnalyzer fallbackAnalyzer) {
        this.properties = properties;
        this.llmClient = llmClient;
        this.fallbackAnalyzer = fallbackAnalyzer;
    }

    @Override
    public AnalysisResult analyze(String resumeText, String targetJobDescription, List<String> requiredSkills) {
        if (!properties.isConfigured()) {
            return fallbackAnalyzer.analyze(resumeText, targetJobDescription, requiredSkills);
        }
        try {
            return llmClient.analyze(resumeText, targetJobDescription, requiredSkills);
        } catch (RuntimeException exception) {
            return fallbackAnalyzer.analyze(resumeText, targetJobDescription, requiredSkills);
        }
    }
}
