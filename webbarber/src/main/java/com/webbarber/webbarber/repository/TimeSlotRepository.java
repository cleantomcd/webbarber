package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {

    @Query("SELECT new com.webbarber.webbarber.dto.StandardTimeSlotDTO(" +
            "t.dayOfWeek, t.amStartTime, t.amEndTime, t.pmStartTime, t.pmEndTime, t.interval) " +
            "FROM timeslot t WHERE t.dayOfWeek = :day")
    StandardTimeSlotDTO findByDayOfWeek(@Param("day") int dayOfWeek);

    @Query(value = "SELECT t FROM timeslot t WHERE t.dayOfWeek = :dayOfWeek")
    Optional<TimeSlot> optionalFindByDayOfWeek(int dayOfWeek);

}
