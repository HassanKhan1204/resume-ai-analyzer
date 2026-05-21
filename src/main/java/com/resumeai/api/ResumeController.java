package com.resumeai.api;

import com.resumeai.api.dto.AnalysisResponse;
import com.resumeai.api.dto.ResumeResponse;
import com.resumeai.service.ResumeWorkflowService;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeWorkflowService workflowService;

    public ResumeController(ResumeWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeResponse analyzeResume(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "targetJobDescription", required = false) @Size(max = 10000) String targetJobDescription,
            @RequestParam(value = "requiredSkills", required = false) List<String> requiredSkills
    ) {
        return workflowService.uploadAndAnalyze(file, targetJobDescription, requiredSkills == null ? List.of() : requiredSkills);
    }

    @GetMapping("/{id}")
    public ResumeResponse getResume(@PathVariable UUID id) {
        return workflowService.getResume(id);
    }

    @GetMapping("/{id}/analysis")
    public AnalysisResponse getAnalysis(@PathVariable UUID id) {
        return workflowService.getAnalysis(id);
    }
}
