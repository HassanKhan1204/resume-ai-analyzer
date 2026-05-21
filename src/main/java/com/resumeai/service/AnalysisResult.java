package com.resumeai.service;

import java.util.List;

public record AnalysisResult(
        List<String> missingSkills,
        List<String> suggestions,
        int jobMatchingScore,
        String summary,
        String analyzer
) {
}
