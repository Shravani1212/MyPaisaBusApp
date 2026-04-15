# Go Bus - Premium Bus Ticket Booking System

A state-of-the-art bus ticket booking and boarding manifest system designed for bus conductors. This project focuses on high-quality code, intuitive UI/UX, and optimized boarding logistics.

## 🚀 Project Overview

The Go Bus application solves the "aisle blockage" problem in bus boarding. By implementing an optimized boarding sequence (Back-to-Front), we minimize the total time taken for all passengers to settle, ensuring a seamless experience for both conductors and travelers.

### Key Features
- **Smart Seat Selection**: Interactive 2x2 seat layout with 15 rows (60 seats total).
- **Intelligent Validation**: Enforces a maximum of 6 seats per mobile number per day.
- **Boarding Optimization**: Automated algorithm sorts passengers by seat distance to minimize boarding delays.
- **Boarding Manifest**: Real-time tracking for conductors with one-click calling and boarding confirmation.
- **Beautiful UI**: Glassmorphic design with tricolor, premium, and nature themes.
- **AI Assistant**: Integrated "Bus Buddy" chatbot for instant support.

---

## 🛠️ Technology Stack

- **Backend**: Java 16+, Spring Boot 2.7.x, Spring Data JPA, Hibernate.
- **Frontend**: Angular 17, TypeScript, Vanilla CSS3.
- **Database**: PostgreSQL (Primary) / H2 (Fallback).
- **DevOps**: Maven, npm.

---

## ⚙️ Setup and Execution Steps

### 1. Authentication
The app now enforces login before booking seats or viewing the boarding manifest.
- Default admin credentials:
  - **Username**: `admin`
  - **Password**: `admin123`
- If not logged in, the app will redirect you to the auth page before booking.

### 2. Database Configuration
The backend can run with an embedded H2 database by default for local development.
- Default H2 config is already enabled in `backend/src/main/resources/application.properties`
- No external database is required to start the app locally

To use PostgreSQL instead, enable the `postgres` profile:
- `mvn spring-boot:run -Dspring-boot.run.profiles=postgres`
- Ensure PostgreSQL is installed and running with these defaults:
  - **Host**: `localhost`
  - **Port**: `5432`
  - **Database Name**: `postgres`
  - **Username**: `postgres`
  - **Password**: `postgres`

### 3. Run the Application (Automated)
We provide a convenient batch script to start both services simultaneously:
1. Open a terminal in the project root directory.
2. Run: `.\run_all.bat`

### 3. Manual Execution

#### Backend (Spring Boot)
1. Navigate to the `backend` folder: `cd backend`
2. Run Maven: `mvn spring-boot:run`
3. The API will be available at `http://localhost:8080`.

#### Frontend (Angular)
1. Navigate to the `frontend` folder: `cd frontend`
2. Install dependencies: `npm install`
3. Start the dev server: `npm start`
4. Access the UI at `http://localhost:4200`.

---

## 🚀 Free Deployment Guidance

### Frontend Deployment (Vercel)
The frontend can be deployed as a static Angular app on Vercel.
1. Push the repository to GitHub.
2. Create a new Vercel project and link the `frontend` directory.
3. Use the build command: `npm run build`
4. Set the output directory to `dist/frontend`.

### Backend Deployment (Free Options)
The backend cannot run on Vercel. Use a separate free/low-cost Java host.
Recommended options:
- **Railway** — free tier credits for Java apps
- **Fly.io** — free allowance for small containers
- **Render** — free web service tier (check availability)
- **Google Cloud Run** — free tier quota
- **Oracle Cloud Always Free** — free compute VMs

The backend is ready for local demo with H2 database by default, so you can deploy without PostgreSQL.

### Wiring the frontend to the deployed backend
Update the backend URL in `frontend/src/assets/env.js` before deploying the frontend.
For example:
```js
window.env = window.env || {
  apiUrl: 'https://your-backend.example.com'
};
```
Then rebuild and redeploy the frontend.

### Optional Docker deployment
- `backend/Dockerfile` is provided for containerizing the backend.
- `frontend/Dockerfile` and `frontend/nginx.conf` are provided for containerizing the frontend.

---

## 🧠 Boarding Optimization Algorithm

### The Problem
When passengers board in a random or front-to-back sequence, those settling in the front rows (A1, B1) block the aisle for those trying to reach the back rows (A15, B15), creating a bottleneck. 

### The Solution (Optimal Sequence)
Our algorithm sorts bookings based on the **farthest seat row** in descending order.
- **Logic**: Rows 1-15 are treated as distances from the front gate.
- **Implementation**: The system calculates the `max(row_number)` for each Booking ID and sorts the manifest list accordingly.
- **Result**: Passengers heading to the back board first, followed by middle, then front. This ensures that while a passenger is settling into their seat, they never block the path of another passenger entering behind them.

---

## 📋 Assumptions Handled
1. **Settling Time**: Each passenger group takes ~60s to settle.
2. **Path Blockage**: While settling, the aisle is blocked for any passenger needing to pass that row.
3. **Single Gate**: All boarding occurs via the front gate only.
4. **Group Boarding**: All passengers under a single Booking ID enter and board together.

---

## 🔒 Design & Quality
- **Error Handling**: Global Exception Handler in Java for graceful API error responses.
- **Responsiveness**: Fully responsive CSS layout using modern Flexbox and CSS Grid.
- **Persistence**: PostgreSQL integration for reliable data storage.
- **Clean Code**: Adheres to SOLID principles and industry-standard naming conventions.

---
*Created as part of the MyPaisaa Technical Assessment.*
