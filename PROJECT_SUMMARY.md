# Project Summary

## Resume AI Analyzer

A Java/Spring Boot backend that lets users upload resumes and receive AI-style feedback: missing skills, improvement suggestions, and a job matching score.

## Why This Project Matters

This project shows practical backend engineering skills:

- building REST APIs
- handling multipart file uploads
- extracting text from PDF and DOCX files
- persisting records in PostgreSQL
- designing a service boundary for LLM API integration
- returning structured JSON responses for frontend or API consumers

## One-Minute Interview Pitch

I built a Spring Boot resume analysis service that accepts resume uploads, extracts text from PDF/DOCX files, stores metadata and analysis results in PostgreSQL, and returns missing skills, suggestions, and a job matching score. The AI integration is designed behind a service interface, so it can use an OpenAI-compatible LLM in production while falling back to deterministic local scoring for demos and tests.

## Key Endpoints

- `GET /` - browser demo
- `GET /api/health` - health check
- `POST /api/resumes/analyze` - upload and analyze resume
- `GET /api/resumes/{id}` - fetch resume metadata and analysis
- `GET /api/resumes/{id}/analysis` - fetch only analysis result

## Technologies

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Apache Tika
- OpenAI-compatible LLM API
