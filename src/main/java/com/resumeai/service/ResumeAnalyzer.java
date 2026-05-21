package com.resumeai.service;

import java.util.List;

public interface ResumeAnalyzer {

    AnalysisResult analyze(String resumeText, String targetJobDescription, List<String> requiredSkills);
}
