package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
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

    @Query("SELECT new com.webbarber.webbarber.entity.Booking(b.userId, b.serviceId, b.date, b.startTime, b.endTime) FROM booking b WHERE b.date = :date AND b.startTime = :startTime")
    Optional<Booking> findByDateAndStartTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    @Query("SELECT b.startTime FROM booking b WHERE b.date = :date")
    List<LocalTime> findStartTimesByDate(@Param("date") LocalDate date);

    @Query("SELECT b FROM booking b WHERE b.date = :date AND :startTime >= b.startTime AND :startTime < b.endTime")
    Optional<Booking> findConflictingBooking(@Param("date") LocalDate date,
                                             @Param("startTime") LocalTime startTime);

    @Query("SELECT new com.webbarber.webbarber.dto.BookingInfoDTO(u.name, s.name, b.date, b.startTime, b.endTime) " +
            "FROM booking b " +
            "JOIN user  u ON b.userId = u.id " +
            "JOIN services  s ON b.serviceId = s.id " +
            "WHERE b.date = :date " +
            "ORDER BY b.date, b.startTime")
    List<BookingInfoDTO> findAllByDate(@Param("date") LocalDate date);


    @Query("SELECT new com.webbarber.webbarber.dto.BookingDTO(b.userId, b.serviceId, b.date, b.startTime, b.endTime) FROM booking b WHERE b.userId = :userId")
    List<BookingDTO> findAllByUserId(String userId);





}
