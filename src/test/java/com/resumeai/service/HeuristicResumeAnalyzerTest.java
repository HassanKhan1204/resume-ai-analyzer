package com.resumeai.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class HeuristicResumeAnalyzerTest {

    private final HeuristicResumeAnalyzer analyzer = new HeuristicResumeAnalyzer();

    @Test
    void reportsMissingSkillsAndScoreFromRequiredSkills() {
        AnalysisResult result = analyzer.analyze(
                "Built Java services with Spring Boot, REST APIs, PostgreSQL, and file upload workflows.",
                "Backend role integrating AI workflows.",
                List.of("Java", "Spring Boot", "REST APIs", "PostgreSQL", "LLM API", "Docker")
        );

        assertThat(result.missingSkills()).containsExactly("LLM API", "Docker");
        assertThat(result.jobMatchingScore()).isEqualTo(67);
        assertThat(result.suggestions()).isNotEmpty();
        assertThat(result.analyzer()).isEqualTo("heuristic-fallback");
    }
}
