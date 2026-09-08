# IdeaForge — Backend

Spring Boot 3 / Java 21 REST API for the AI Startup Incubator project.

## Stack
- Spring Boot 3.3 (Web, Data JPA, Security, Validation)
- PostgreSQL
- JWT auth (jjwt)
- Google Gemini API (`gemini-1.5-flash`) for all AI generation
- iText 7 for PDF report export

## Project layout
```
controller/   REST endpoints
service/      business logic + GeminiService (single AI integration point)
model/        JPA entities
repository/   Spring Data JPA repositories
dto/          request/response payloads
security/     JWT filter + util
config/       CORS + Spring Security config
exception/    global error handling
```

## Modules → endpoints

| Station          | Feature              | Endpoint                                  |
|------------------|-----------------------|-------------------------------------------|
| Auth             | Register / Login      | `POST /api/auth/register`, `/api/auth/login` |
| —                | Startups (CRUD-ish)   | `POST/GET /api/startups`, `GET /api/startups/{id}` |
| The Forge        | Idea Generator         | `POST /api/ideas/generate`, `GET /api/ideas` |
| The Forge        | Validation             | `POST/GET /api/validation/{startupId}` |
| The Blueprint    | Business Model Canvas  | `POST/GET /api/business-model/{startupId}` |
| The Blueprint    | Competitor Analysis    | `POST/GET /api/competitors/{startupId}` |
| The Ledger       | Financial Plan         | `POST/GET /api/financial-plan/{startupId}` |
| The Ledger       | MVP Roadmap            | `POST/GET /api/mvp-plan/{startupId}` |
| The Showcase     | Investor Pitch         | `POST/GET /api/pitch/{startupId}` |
| The Showcase     | Report Export (PDF)    | `GET /api/reports/{startupId}/pdf` |

All endpoints except `/api/auth/**` require `Authorization: Bearer <jwt>`.

## Running locally

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE incubator_db;
   ```
2. Set environment variables (or edit `application.yml` directly):
   ```
   DB_USERNAME=postgres
   DB_PASSWORD=yourpassword
   JWT_SECRET=<a long random string, 32+ bytes>
   GEMINI_API_KEY=<your Gemini API key>
   ```
3. Build & run:
   ```
   mvn spring-boot:run
   ```
   Server starts on `http://localhost:8080`. Tables are auto-created via `ddl-auto: update`.

If `GEMINI_API_KEY` isn't set, AI endpoints still respond (200) but return a placeholder
string instead of generated content — useful for testing the rest of the stack without
burning API quota.

## Notes on design decisions
- **`GeminiService`** is the single chokepoint for all AI calls. Every module's service
  builds a prompt and asks for strict JSON back, so adding a new AI-powered module later
  is just: write a prompt, parse the JSON, save an entity.
- Every generation endpoint is `POST` (creates a new row) and has a paired `GET` for the
  latest result, so you keep a history instead of overwriting — useful for showing
  "regenerate" as a real action in the UI.
- `ReportService` pulls from every module's `getLatest()` and silently skips any module
  that hasn't been generated yet, so the PDF export works at any stage of a startup's
  workspace, not just when everything is filled in.
