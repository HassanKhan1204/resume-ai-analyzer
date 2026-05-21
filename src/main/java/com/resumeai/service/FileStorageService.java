package com.resumeai.service;

import com.resumeai.config.StorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(StorageProperties properties) {
        this.uploadDir = Path.of(properties.uploadDir()).toAbsolutePath().normalize();
    }

    public StoredFile store(MultipartFile file) {
        try {
            Files.createDirectories(uploadDir);
            String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "resume" : file.getOriginalFilename());
            String extension = extensionOf(original);
            String storedName = UUID.randomUUID() + extension;
            Path destination = uploadDir.resolve(storedName).normalize();

            if (!destination.startsWith(uploadDir)) {
                throw new IllegalArgumentException("Invalid upload path.");
            }

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            return new StoredFile(original, destination);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store uploaded resume.", exception);
        }
    }

    private String extensionOf(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}
