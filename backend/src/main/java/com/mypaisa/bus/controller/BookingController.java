package com.mypaisa.bus.controller;

import com.mypaisa.bus.model.Booking;
import com.mypaisa.bus.service.BookingService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.OPTIONS}, allowedHeaders = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> bookSeats(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getBookingsForDate(date));
    }

    @PatchMapping("/{id}/board")
    public ResponseEntity<Booking> markBoarded(@PathVariable("id") UUID id) {
        System.out.println("PATCH: Marking ID as boarded: " + id);
        return ResponseEntity.ok(bookingService.markAsBoarded(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Booking> editBooking(@PathVariable("id") UUID id, @Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.editBooking(id, booking));
    }
}

