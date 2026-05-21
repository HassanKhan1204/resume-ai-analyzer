package com.resumeai.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeai.config.LlmProperties;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenAiCompatibleLlmClient implements LlmClient {

    private final LlmProperties properties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OpenAiCompatibleLlmClient(LlmProperties properties, RestClient.Builder builder, ObjectMapper objectMapper) {
        this.properties = properties;
        this.restClient = builder.baseUrl(properties.baseUrl()).build();
        this.objectMapper = objectMapper;
    }

    @Override
    public AnalysisResult analyze(String resumeText, String targetJobDescription, List<String> requiredSkills) {
        Map<String, Object> body = Map.of(
                "model", properties.model(),
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt()),
                        Map.of("role", "user", "content", userPrompt(resumeText, targetJobDescription, requiredSkills))
                )
        );

        ChatCompletionResponse response = restClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + properties.apiKey())
                .body(body)
                .retrieve()
                .body(ChatCompletionResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("LLM returned no analysis.");
        }

        String content = response.choices().get(0).message().content();
        try {
            LlmAnalysis analysis = objectMapper.readValue(content, LlmAnalysis.class);
            return new AnalysisResult(
                    analysis.missingSkills() == null ? List.of() : analysis.missingSkills(),
                    analysis.suggestions() == null ? List.of() : analysis.suggestions(),
                    clamp(analysis.jobMatchingScore()),
                    analysis.summary() == null ? "LLM generated resume analysis." : analysis.summary(),
                    "llm:" + properties.model()
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("LLM response was not valid JSON.", exception);
        }
    }

    private String systemPrompt() {
        return """
                You are a technical recruiter assistant. Analyze resumes for backend engineering roles.
                Return only JSON with keys: missingSkills, suggestions, jobMatchingScore, summary.
                missingSkills and suggestions must be arrays of strings. jobMatchingScore must be 0-100.
                """;
    }

    private String userPrompt(String resumeText, String targetJobDescription, List<String> requiredSkills) {
        return """
                Resume:
                %s

                Target job description:
                %s

                Required skills:
                %s
                """.formatted(
                truncate(resumeText, 12000),
                targetJobDescription == null ? "Not provided" : truncate(targetJobDescription, 5000),
                requiredSkills == null || requiredSkills.isEmpty() ? "Infer from the job description." : String.join(", ", requiredSkills)
        );
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private int clamp(int score) {
        return Math.max(0, Math.min(100, score));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatCompletionResponse(List<Choice> choices) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(Message message) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Message(String content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LlmAnalysis(
            List<String> missingSkills,
            List<String> suggestions,
            int jobMatchingScore,
            String summary
    ) {
    }
}
