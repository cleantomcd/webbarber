package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.Scheduling;
import com.webbarber.webbarber.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
    @Nonnull
    Optional<Scheduling> findById(@Nonnull String id);
    List<Scheduling> findAllByUser(@Nonnull User user);
}
