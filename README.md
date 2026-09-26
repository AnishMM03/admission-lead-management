# Admission Lead Management

A full-stack Java Spring Boot + React project for managing educational admission leads from first contact through follow-up and conversion.

## Tech stack
- Backend: Java 17, Spring Boot 3, JPA, H2 database
- Frontend: React + Vite
- API: REST endpoints

## Project structure
- `src/` - backend Spring Boot application
- `frontend/` - React frontend

## Backend setup
1. Open terminal in project root.
2. Run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. Backend will run on `http://localhost:8080`
4. H2 console: `http://localhost:8080/h2-console`

## Frontend setup
1. Open terminal in `frontend/`.
2. Run:
   ```bash
   npm install
   npm run dev
   ```
3. Frontend will run on `http://localhost:5173`

## Default API configuration
Frontend expects backend at:
- `http://localhost:8080`

## Features included
- Add new leads
- Assign counsellors
- View dashboard summary
- Track lead lifecycle statuses
- Schedule follow-ups
- Manage counsellors
- Handle course preferences
- Manage lead sources

## Sample lead
```json
{
  "fullName": "Aisha Khan",
  "email": "aisha@example.com",
  "phone": "9876543210",
  "city": "Bengaluru",
  "address": "MG Road",
  "sourceName": "Website",
  "sourceChannel": "Website",
  "coursePreferences": ["B.Tech", "BBA"],
  "assignedCounselorId": 1
}
```
