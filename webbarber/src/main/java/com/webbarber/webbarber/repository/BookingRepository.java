package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Nonnull
    Optional<Booking> findById(@Nonnull String id);
    List<Booking> findAllByUser(@Nonnull User user);
}
