package com.mypaisa.bus.repository;

import com.mypaisa.bus.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
    List<Booking> findByTravelDate(LocalDate travelDate);

    @Query("SELECT SUM(size(b.seats)) FROM Booking b WHERE b.mobileNumber = :mobile AND b.travelDate = :date")
    Long countTotalSeatsByMobileAndDate(@Param("mobile") String mobile, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b JOIN b.seats s WHERE b.travelDate = :date AND s IN :seats")
    List<Booking> findConflictingBookings(@Param("date") LocalDate date, @Param("seats") List<String> seats);
}
