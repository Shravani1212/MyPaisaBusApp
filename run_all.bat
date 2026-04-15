@echo off
echo Starting Bus Ticket Booking System...

echo Starting Backend (Spring Boot)...
start cmd /k "cd backend && mvn spring-boot:run"

echo Starting Frontend (Angular)...
start cmd /k "cd frontend && npm install && npm start"

echo.
echo Both applications are starting.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:4200
echo.
pause
