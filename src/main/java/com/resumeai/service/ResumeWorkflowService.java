package com.resumeai.service;

import com.resumeai.api.dto.AnalysisResponse;
import com.resumeai.api.dto.ResumeResponse;
import com.resumeai.domain.ResumeAnalysis;
import com.resumeai.domain.ResumeDocument;
import com.resumeai.repository.ResumeDocumentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeWorkflowService {

    private final ResumeDocumentRepository resumeRepository;
    private final FileStorageService fileStorageService;
    private final ResumeTextExtractor textExtractor;
    private final ResumeAnalyzer resumeAnalyzer;

    public ResumeWorkflowService(
            ResumeDocumentRepository resumeRepository,
            FileStorageService fileStorageService,
            ResumeTextExtractor textExtractor,
            ResumeAnalyzer resumeAnalyzer
    ) {
        this.resumeRepository = resumeRepository;
        this.fileStorageService = fileStorageService;
        this.textExtractor = textExtractor;
        this.resumeAnalyzer = resumeAnalyzer;
    }

    @Transactional
    public ResumeResponse uploadAndAnalyze(MultipartFile file, String targetJobDescription, List<String> requiredSkills) {
        validateUpload(file);

        StoredFile storedFile = fileStorageService.store(file);
        String extractedText = textExtractor.extractText(storedFile.path());

        ResumeDocument resume = new ResumeDocument(
                file.getOriginalFilename(),
                safeContentType(file),
                file.getSize(),
                storedFile.path().toString(),
                extractedText
        );

        AnalysisResult result = resumeAnalyzer.analyze(extractedText, targetJobDescription, requiredSkills);
        ResumeAnalysis analysis = new ResumeAnalysis(
                resume,
                result.missingSkills(),
                result.suggestions(),
                result.jobMatchingScore(),
                result.summary(),
                result.analyzer()
        );
        resume.setAnalysis(analysis);

        return toResumeResponse(resumeRepository.save(resume));
    }

    @Transactional(readOnly = true)
    public ResumeResponse getResume(UUID id) {
        return resumeRepository.findById(id)
                .map(this::toResumeResponse)
                .orElseThrow(() -> new ResumeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public AnalysisResponse getAnalysis(UUID id) {
        ResumeDocument resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(id));
        return toAnalysisResponse(resume);
    }

    private void validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Upload a non-empty resume file.");
        }

        String contentType = safeContentType(file);
        if (!List.of(
                "application/pdf",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
        ).contains(contentType)) {
            throw new UnsupportedResumeFileException(contentType);
        }
    }

    private String safeContentType(MultipartFile file) {
        return file.getContentType() == null ? "application/octet-stream" : file.getContentType();
    }

    private ResumeResponse toResumeResponse(ResumeDocument resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getOriginalFilename(),
                resume.getContentType(),
                resume.getSizeBytes(),
                resume.getUploadedAt(),
                toAnalysisResponse(resume)
        );
    }

    private AnalysisResponse toAnalysisResponse(ResumeDocument resume) {
        ResumeAnalysis analysis = resume.getAnalysis();
        if (analysis == null) {
            return null;
        }
        return new AnalysisResponse(
                resume.getId(),
                analysis.getMissingSkills(),
                analysis.getSuggestions(),
                analysis.getJobMatchingScore(),
                analysis.getSummary(),
                analysis.getAnalyzer(),
                analysis.getAnalyzedAt()
        );
    }
}
