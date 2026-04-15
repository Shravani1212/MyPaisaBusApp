package com.mypaisa.bus.service;

import com.mypaisa.bus.model.Booking;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendBookingConfirmation(Booking booking) {
        logger.info("Simulating sending email confirmation...");
        try {
            // Simulate network delay
            Thread.sleep(2000);
            
            String message = String.format(
                "Subject: Booking Confirmation - ID: %s\n" +
                "To: User associated with %s\n\n" +
                "Dear Customer,\n\n" +
                "Your bus booking for %s is confirmed.\n" +
                "Seats: %s\n\n" +
                "Thank you for choosing MyPaisaa Bus!",
                booking.getId(),
                booking.getMobileNumber(),
                booking.getTravelDate(),
                String.join(", ", booking.getSeats())
            );
            
            logger.info("Email Sent Successfully:\n{}", message);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Email simulation interrupted", e);
        }
    }
}
