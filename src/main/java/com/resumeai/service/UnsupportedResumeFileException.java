package com.resumeai.service;

public class UnsupportedResumeFileException extends RuntimeException {

    public UnsupportedResumeFileException(String contentType) {
        super("Unsupported resume file type: " + contentType + ". Upload a PDF, DOCX, or plain text file.");
    }
}
