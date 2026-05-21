package com.resumeai.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
public class ResumeTextExtractor {

    private final Tika tika = new Tika();

    public String extractText(Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            String text = tika.parseToString(input);
            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("Could not extract text from the uploaded resume.");
            }
            return text.trim();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read uploaded resume.", exception);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Could not parse uploaded resume text.", exception);
        }
    }
}
