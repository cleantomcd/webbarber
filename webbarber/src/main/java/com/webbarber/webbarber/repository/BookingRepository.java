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

    @Query("SELECT new com.webbarber.webbarber.entity.Booking(b.userId, b.barberId, b.serviceId, b.date, b.startTime, b.endTime) FROM Booking b WHERE b.barberId = :barberId AND b.date = :date AND b.startTime = :startTime")
    Optional<Booking> findByBarberIdAndDateAndStartTime(@Param("barberId") String barberId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    @Query("SELECT b.startTime FROM Booking b WHERE b.barberId = :barberId AND b.date = :date")
    List<LocalTime> findStartTimesByBarberIdAndDate(@Param("barberId") String barberId, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.barberId = :barberId AND b.date = :date AND :startTime >= b.startTime AND :startTime < b.endTime")
    Optional<Booking> findConflictingBooking(@Param("barberId") String barberId, @Param("date") LocalDate date,
                                             @Param("startTime") LocalTime startTime);

    @Query("SELECT new com.webbarber.webbarber.dto.BookingInfoDTO(u.name, br.name, s.name, b.date, b.startTime, b.endTime) " +
            "FROM Booking b " +
            "JOIN User u ON b.userId = u.id " +
            "JOIN Service s ON b.serviceId = s.id " +
            "JOIN Barber br ON b.barberId = br.id " +
            "WHERE b.date = :date AND br.id = :barberId " +
            "ORDER BY b.date, b.startTime")
    List<BookingInfoDTO> findAllByBarberIdAndDate(@Param("barberId") String barberId, @Param("date") LocalDate date);


    @Query("SELECT new com.webbarber.webbarber.dto.BookingDTO(b.userId, b.barberId, b.serviceId, b.date, b.startTime, b.endTime) FROM Booking b WHERE b.barberId = :barberId AND b.userId = :userId")
    List<BookingDTO> findAllByBarberIdAndUserId(@Param("barberId") String barberId, @Param("userId") String userId);





}
