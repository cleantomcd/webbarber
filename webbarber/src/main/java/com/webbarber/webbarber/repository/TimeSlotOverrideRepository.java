package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.TimeSlotOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotOverrideRepository extends JpaRepository<TimeSlotOverride, String> {
    Optional<TimeSlotOverride> findByDate(LocalDate date);
    TimeSlotOverride findDTOByDate(LocalDate date);
    @Query(value = "SELECT ts.closed_slots " +
            "FROM timeslot_override t " +
            "JOIN timeslot_override_closed_slots ts " +
            "ON t.id = ts.timeslot_override_id " +
            "WHERE :time = ANY(SELECT ts.closed_slots FROM timeslot_override_closed_slots ts)",
            nativeQuery = true)
    List<String> findClosedSlotsContainingTime(@Param("time") String time);


}

