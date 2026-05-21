package com.resumeai.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class HeuristicResumeAnalyzer implements ResumeAnalyzer {

    private static final List<String> DEFAULT_SKILLS = List.of(
            "Java", "Spring Boot", "REST APIs", "PostgreSQL", "Docker", "AWS", "JUnit", "CI/CD"
    );

    @Override
    public AnalysisResult analyze(String resumeText, String targetJobDescription, List<String> requiredSkills) {
        List<String> targetSkills = requiredSkills == null || requiredSkills.isEmpty()
                ? DEFAULT_SKILLS
                : normalizeSkills(requiredSkills);

        String haystack = (resumeText + " " + nullToEmpty(targetJobDescription)).toLowerCase(Locale.ROOT);
        List<String> missingSkills = targetSkills.stream()
                .filter(skill -> !haystack.contains(skill.toLowerCase(Locale.ROOT)))
                .toList();

        int matched = targetSkills.size() - missingSkills.size();
        int score = targetSkills.isEmpty() ? 70 : Math.round((matched * 100f) / targetSkills.size());
        score = Math.max(15, Math.min(98, score));

        List<String> suggestions = new ArrayList<>();
        if (!missingSkills.isEmpty()) {
            suggestions.add("Add evidence for these role-critical skills: " + String.join(", ", missingSkills) + ".");
        }
        suggestions.add("Quantify backend impact with metrics such as latency, throughput, reliability, or deployment frequency.");
        suggestions.add("Use project bullets that connect Java, APIs, database design, file handling, and AI workflow outcomes.");

        String summary = "Resume was analyzed against " + targetSkills.size()
                + " target skills and matched " + matched + " of them.";

        return new AnalysisResult(missingSkills, suggestions, score, summary, "heuristic-fallback");
    }

    private List<String> normalizeSkills(List<String> requiredSkills) {
        Set<String> unique = new LinkedHashSet<>();
        for (String skill : requiredSkills) {
            if (skill != null && !skill.isBlank()) {
                unique.add(skill.trim());
            }
        }
        return List.copyOf(unique);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
