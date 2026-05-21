package com.resumeai;

import com.resumeai.config.LlmProperties;
import com.resumeai.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, LlmProperties.class})
public class ResumeAiAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeAiAnalyzerApplication.class, args);
    }
}
