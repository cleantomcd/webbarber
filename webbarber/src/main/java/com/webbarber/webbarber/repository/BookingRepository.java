package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.Booking;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Nonnull
    Optional<Booking> findById(@Nonnull String id);
    @Query("SELECT new com.webbarber.webbarber.dto.BookingDTO(b.userId, b.serviceId, b.date, b.startTime, b.endTime) FROM booking b WHERE b.userId = :userId")
    List<Booking> findAllByUserId(@Param("userId") String userId);
    Optional<Booking> findByDateAndStartTime(LocalDate date, LocalTime startTime);
    List<Booking> findAllByDate(LocalDate date);
    @Query("SELECT b.startTime FROM booking b WHERE b.date = :date")
    List<LocalTime> findStartTimesByDate(@Param("date") LocalDate date);


}
