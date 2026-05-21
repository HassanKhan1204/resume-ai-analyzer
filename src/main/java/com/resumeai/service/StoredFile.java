package com.resumeai.service;

import java.nio.file.Path;

public record StoredFile(String originalFilename, Path path) {
}
