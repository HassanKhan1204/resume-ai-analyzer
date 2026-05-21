package com.resumeai.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AnalysisResponse(
        UUID resumeId,
        List<String> missingSkills,
        List<String> suggestions,
        int jobMatchingScore,
        String summary,
        String analyzer,
        Instant analyzedAt
) {
}
