package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.TimeSlotOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimeSlotOverrideRepository extends JpaRepository<TimeSlotOverride, String> {
    Optional<TimeSlotOverride> findByDate(LocalDate date);
    TimeSlotOverride findDTOByDate(LocalDate date);
}

