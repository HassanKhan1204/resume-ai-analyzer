package com.resumeai.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ResumeResponse(
        UUID id,
        String originalFilename,
        String contentType,
        long sizeBytes,
        Instant uploadedAt,
        AnalysisResponse analysis
) {
}
