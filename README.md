# Resume AI Analyzer

AI-powered resume analysis application built with Java and Spring Boot. Users can upload resumes and receive missing skills, improvement suggestions, and a job matching score based on a target role.

Built as a portfolio project to demonstrate backend engineering concepts including REST APIs, file processing, PostgreSQL persistence, and AI integration.

---

## Features

- Upload PDF, DOCX, or TXT resumes
- Extract text using Apache Tika
- Identify missing skills
- Generate improvement suggestions
- Calculate a job matching score
- Store resume history in PostgreSQL
- LLM integration with fallback support
- Browser demo + REST API endpoints

---

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA

**Database**
- PostgreSQL

**AI + Processing**
- Apache Tika
- OpenAI-compatible LLM API

**Infrastructure**
- Maven
- Docker

---

## Demo

Open:

http://localhost:8080

Flow:

1. Upload a resume
2. Backend extracts resume text
3. Resume content is analyzed against target skills
4. Analysis is stored in PostgreSQL
5. Results are returned

Example output:

- Missing skills
- Resume suggestions
- Job matching score

---

## API

### Health Check

```bash
curl http://localhost:8080/api/health
```

### Analyze Resume

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
  "missingSkills": ["LLM API"],
  "suggestions": [
    "Add evidence for role-critical skills.",
    "Quantify impact with metrics."
  ],
  "jobMatchingScore": 80
}
```

### Fetch Resume

```bash
curl http://localhost:8080/api/resumes/{resumeId}
```

### Fetch Analysis

```bash
curl http://localhost:8080/api/resumes/{resumeId}/analysis
```

---

## Installation

Clone the repository:

```bash
git clone https://github.com/yourusername/resume-ai-analyzer.git
cd resume-ai-analyzer
```

Start PostgreSQL:

```bash
docker compose up -d postgres
```

Run application:

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

---

## Docker Setup

Start full application:

```bash
docker compose up -d --build
```

Stop:

```bash
docker compose down
```

---

## Environment Variables

Optional AI configuration:

```powershell
$env:LLM_ENABLED="true"
$env:LLM_API_KEY="your_api_key"
$env:LLM_MODEL="gpt-4o-mini"
```

Optional:

```powershell
$env:LLM_BASE_URL="https://api.openai.com/v1"
```

See:

```text
.env.example
```

---

## Architecture

```text
Browser UI
     |
     v
Spring Boot Controller
     |
     +--> FileStorageService
     |
     +--> ResumeTextExtractor
     |
     +--> ResumeAnalyzer
     |
     v
PostgreSQL
```

---

## Future Improvements

- Resume keyword highlighting
- Embedding/vector similarity search
- Authentication and user accounts
- Resume analytics dashboard
- Deployment to AWS

---

## License

MIT
