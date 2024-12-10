package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.entity.Booking;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Nonnull
    Optional<Booking> findById(@Nonnull String id);
    @Query("SELECT new com.webbarber.webbarber.dto.BookingDTO(s.userId, s.serviceId, s.start, s.estimatedEnd) FROM scheduling s WHERE s.userId = :userId")
    List<Booking> findAllByUserId(@Param("userId") String userId);
}
