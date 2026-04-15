# Bus Ticket Booking System - MyPaisaa Assessment

A full-stack Bus Ticket Booking System for Bus Conductors, featuring a premium UI/UX, real-time seat selection, and an optimal boarding sequence algorithm.

## Technology Stack
- **Backend**: Java 17, Spring Boot 3, Spring Data JPA, H2/PostgreSQL.
- **Frontend**: Angular 17, CSS3 (Glassmorphism), HTML5.
- **Database**: H2 (In-memory for demo) / PostgreSQL (Supported).

## Key Features
1. **Seat Selection**: 2x2 arrangement with 15 rows (60 seats total).
2. **Boarding Optimization**: Algorithm calculates the optimal boarding sequence (farthest seats first) to minimize aisle blockage.
3. **Multi-language Support**: Fully supports **English, Hindi, and Telugu** (selectable from the header).
4. **Map Integration**: Integrated interactive route map showing bus location and terminal points.
5. **Chatbot Support**: "Bus Buddy" AI assistant for handling FAQs and booking queries.
6. **WhatsApp & Email**: Automatic email confirmation (simulated in backend) and one-click WhatsApp sharing for ticket details.
7. **Boarding Tracking**: Real-time list with "Call" and "Board" actions for conductors.

## Boarding Algorithm Explanation
The system implements a distance-based boarding algorithm. Since the bus has a single front entrance, passengers settling into front seats block the aisle for those going to the back.
- **Rule**: Passengers in the farthest rows (Row 15) board first.
- **Logic**: Bookings are sorted by `max(row_number)` in descending order.
- **Total Boarding Time**: Minimized by ensuring no passenger is blocked by someone settling in front of them.

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- Maven

### Running the Backend
1. Navigate to the `backend` directory.
2. Run `./mvnw spring-boot:run` (or use your IDE).
3. The API will be available at `http://localhost:8080`.
4. H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:busdb`).

### Running the Frontend
1. Navigate to the `frontend` directory.
2. Run `npm install` (First time only).
3. Run `npm start`.
4. The UI will be available at `http://localhost:4200`.

## Submission Details
- **Project Structure**: Multi-module (Backend/Frontend).
- **Quality**: Follows industry-standard naming conventions, clean code principles, and proper error handling.
