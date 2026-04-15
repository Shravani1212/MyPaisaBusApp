package com.mypaisa.bus.service;

import com.mypaisa.bus.model.Booking;
import com.mypaisa.bus.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    public Booking createBooking(Booking booking) {
        // Validation: Max 6 seats constraint (Total across all bookings for the day)
        Long totalSeatsAlreadyBooked = bookingRepository.countTotalSeatsByMobileAndDate(
                booking.getMobileNumber(), booking.getTravelDate());
        
        long total = (totalSeatsAlreadyBooked != null ? totalSeatsAlreadyBooked : 0) + booking.getSeats().size();
        logger.debug("Booking attempt for {}: current seats={}, new seats={}, total={}", 
            booking.getMobileNumber(), totalSeatsAlreadyBooked, booking.getSeats().size(), total);
        
        if (total > 6) {
            throw new RuntimeException("Maximum 6 seats can be booked per mobile number per day. You have already booked " + (totalSeatsAlreadyBooked != null ? totalSeatsAlreadyBooked : 0) + " seats.");
        }

        // Conflict check: Are any seats already booked?
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getTravelDate(), booking.getSeats());
        
        if (!conflicts.isEmpty()) {
            logger.warn("Booking conflict detected for seats: {}", booking.getSeats());
            throw new RuntimeException("One or more selected seats are already booked.");
        }

        Booking savedBooking = bookingRepository.save(booking);
        emailService.sendBookingConfirmation(savedBooking);
        return savedBooking;
    }

    public Booking editBooking(UUID id, Booking booking) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate seat count for this mobile on the selected date, excluding current booking
        Long totalSeatsAlreadyBooked = bookingRepository.countTotalSeatsByMobileAndDateExcludingId(
                booking.getMobileNumber(), booking.getTravelDate(), id);
        long total = (totalSeatsAlreadyBooked != null ? totalSeatsAlreadyBooked : 0) + booking.getSeats().size();
        logger.debug("Edit booking attempt for {}: existing seats={}, new seats={}, total={}",
                booking.getMobileNumber(), totalSeatsAlreadyBooked, booking.getSeats().size(), total);

        if (total > 6) {
            throw new RuntimeException("Maximum 6 seats can be booked per mobile number per day. You have already booked "
                    + (totalSeatsAlreadyBooked != null ? totalSeatsAlreadyBooked : 0) + " seats.");
        }

        // Check for seat conflicts excluding current booking
        List<Booking> conflicts = bookingRepository.findConflictingBookingsExcludingId(
                booking.getTravelDate(), booking.getSeats(), id);
        if (!conflicts.isEmpty()) {
            logger.warn("Booking edit conflict detected for seats: {}", booking.getSeats());
            throw new RuntimeException("One or more selected seats are already booked.");
        }

        existing.setTravelDate(booking.getTravelDate());
        existing.setMobileNumber(booking.getMobileNumber());
        existing.setSeats(booking.getSeats());
        existing.setBoarded(booking.isBoarded());
        Booking updatedBooking = bookingRepository.save(existing);
        emailService.sendBookingConfirmation(updatedBooking);
        return updatedBooking;
    }

    public List<Booking> getBookingsForDate(LocalDate date) {
        List<Booking> bookings = bookingRepository.findByTravelDate(date);
        
        // Boarding Optimization Algorithm: Sort by farthest seat (highest row number)
        return bookings.stream()
                .sorted((b1, b2) -> {
                    int maxRow1 = getMaxRow(b1.getSeats());
                    int maxRow2 = getMaxRow(b2.getSeats());
                    // Higher row number first (farthest seats)
                    return Integer.compare(maxRow2, maxRow1);
                })
                .collect(Collectors.toList());
    }

    public Booking markAsBoarded(UUID id) {
        logger.info("Service: Looking for booking ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Service: Booking not found for ID: {}", id);
                    return new RuntimeException("Booking not found");
                });
        booking.setBoarded(true);
        return bookingRepository.save(booking);
    }

    private int getMaxRow(List<String> seats) {
        return seats.stream()
                .map(s -> {
                    // Extract row number from seat string e.g., "15A" -> 15
                    String rowStr = s.replaceAll("[^0-9]", "");
                    return Integer.parseInt(rowStr);
                })
                .max(Integer::compare)
                .orElse(0);
    }
}
