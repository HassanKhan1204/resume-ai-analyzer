# Resume AI Analyzer

Resume AI Analyzer is a presentable Spring Boot portfolio project where users upload resumes and receive missing skills, improvement suggestions, and a job matching score.

The app includes both a REST API and a simple browser demo served by Spring Boot at `http://localhost:8080`.

## What It Does

- missing skills
- resume improvement suggestions
- job matching score
- uploaded resume history in PostgreSQL

This is built as an interview-friendly Java backend project: REST APIs, file handling, PostgreSQL persistence, and an LLM API integration boundary.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web REST APIs
- Spring Data JPA
- PostgreSQL
- Apache Tika for PDF, DOCX, and text extraction
- OpenAI-compatible LLM API client

## Demo Flow

1. User opens `http://localhost:8080`
2. User uploads a PDF, DOCX, or TXT resume
3. Backend extracts resume text with Apache Tika
4. Analyzer compares resume content against target skills
5. API returns missing skills, suggestions, score, and persisted resume ID

## Architecture

```text
Browser UI
   |
   v
Spring Boot REST Controller
   |
   +--> FileStorageService saves the upload
   |
   +--> ResumeTextExtractor extracts text with Apache Tika
   |
   +--> ResumeAnalyzer calls LLM client or fallback analyzer
   |
   v
PostgreSQL stores resume metadata and analysis
```

## API

### Health Check

```bash
curl http://localhost:8080/api/health
```

### Analyze a Resume

```bash
curl -X POST http://localhost:8080/api/resumes/analyze \
  -F "file=@resume.pdf" \
  -F "targetJobDescription=Backend engineer role using Java, Spring Boot, PostgreSQL, and AI workflows." \
  -F "requiredSkills=Java" \
  -F "requiredSkills=Spring Boot" \
  -F "requiredSkills=REST APIs" \
  -F "requiredSkills=PostgreSQL" \
  -F "requiredSkills=LLM API"
```

Example response:

```json
{
  "id": "7e7d7829-01fa-4d4a-8303-f0edcfd59ed3",
  "originalFilename": "resume.pdf",
  "contentType": "application/pdf",
  "sizeBytes": 152333,
  "uploadedAt": "2026-05-21T17:30:00Z",
  "analysis": {
    "resumeId": "7e7d7829-01fa-4d4a-8303-f0edcfd59ed3",
    "missingSkills": ["LLM API"],
    "suggestions": [
      "Add evidence for these role-critical skills: LLM API.",
      "Quantify backend impact with metrics such as latency, throughput, reliability, or deployment frequency."
    ],
    "jobMatchingScore": 80,
    "summary": "Resume was analyzed against 5 target skills and matched 4 of them.",
    "analyzer": "heuristic-fallback",
    "analyzedAt": "2026-05-21T17:30:02Z"
  }
}
```

### Fetch a Resume

```bash
curl http://localhost:8080/api/resumes/{resumeId}
```

### Fetch Only Analysis

```bash
curl http://localhost:8080/api/resumes/{resumeId}/analysis
```

## Run Locally

### Option 1: Docker Demo Mode

This is the easiest way to keep the project available without leaving a PowerShell tab running.

Start the backend and PostgreSQL in the background:

```bash
docker compose up -d --build
```

Open:

```text
http://localhost:8080
```

Check running containers:

```bash
docker compose ps
```

Stop everything:

```bash
docker compose down
```

### Option 2: Maven Development Mode

Start PostgreSQL only:

```bash
docker compose up -d postgres
```

Run the app:

```bash
mvn spring-boot:run
```

The app starts at `http://localhost:8080`.

Open the browser demo:

```text
http://localhost:8080
```

Environment variables are documented in `.env.example`.

## Enable the LLM API

By default, the app uses a deterministic fallback analyzer so demos work without credentials.

To call an OpenAI-compatible chat completions API:

```bash
$env:LLM_ENABLED="true"
$env:LLM_API_KEY="your_api_key"
$env:LLM_MODEL="gpt-4o-mini"
mvn spring-boot:run
```

Optional:

```bash
$env:LLM_BASE_URL="https://api.openai.com/v1"
```

## Interview Version

Short version:

> I integrated AI into a Java backend service and designed REST APIs for resume analysis workflows.

Expanded version:

> I built a Spring Boot service where users upload PDF, DOCX, or text resumes. The backend stores file metadata in PostgreSQL, extracts resume text with Apache Tika, sends structured prompts to an LLM API, and returns missing skills, improvement suggestions, and a job matching score through REST endpoints. I also added a fallback analyzer so the workflow remains testable when the LLM provider is unavailable.

Resume bullets:

- Built a Spring Boot REST API for resume upload and AI-assisted job matching analysis.
- Integrated file handling, text extraction, PostgreSQL persistence, and an OpenAI-compatible LLM client.
- Designed analysis responses that return missing skills, targeted suggestions, and a normalized job matching score.

## Demo Script

Use this when presenting the project:

> This project simulates a resume screening workflow. The user uploads a resume from the browser, and the Spring Boot backend accepts the multipart file, extracts the text, analyzes it against a target backend role, and stores the result in PostgreSQL. The analysis layer is behind an interface, so in production it can call an LLM API, while local demos can use a deterministic fallback. The output gives recruiters or candidates a missing skills list, improvement suggestions, and a job matching score.

## Portfolio Highlights

- **AI integration:** Prompt-ready LLM client with JSON response handling.
- **Java backend:** Spring Boot controllers, services, validation, exception handling, and JPA persistence.
- **File handling:** Multipart upload, file storage, PDF/DOCX/TXT text extraction.
- **Database:** PostgreSQL-backed resume and analysis records.
- **Demo-ready:** Browser UI plus REST API examples.
