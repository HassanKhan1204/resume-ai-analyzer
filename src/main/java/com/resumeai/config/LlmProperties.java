package com.resumeai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.llm")
public record LlmProperties(boolean enabled, String apiKey, String baseUrl, String model) {

    public boolean isConfigured() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }
}
