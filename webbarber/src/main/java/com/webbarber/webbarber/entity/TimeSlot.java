package com.webbarber.webbarber.entity;


import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;


@Table(name = "timeslot")
@Entity(name = "timeslot")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int dayOfWeek;
    private LocalTime amStartTime;
    private LocalTime amEndTime;
    private LocalTime pmStartTime;
    private LocalTime pmEndTime;
    private int interval;

    public TimeSlot() {}

    public TimeSlot(StandardTimeSlotDTO standardTimeSlotDTO) {
        this.dayOfWeek =  standardTimeSlotDTO.dayOfWeek();
        this.amStartTime = standardTimeSlotDTO.amStartTime();
        this.amEndTime = standardTimeSlotDTO.amEndTime();
        this.pmStartTime = standardTimeSlotDTO.pmStartTime();
        this.pmEndTime = standardTimeSlotDTO.pmEndTime();
        this.interval = standardTimeSlotDTO.interval();
    }

    public String getId() {
        return id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getAmStartTime() {
        return amStartTime;
    }

    public void setAmStartTime(LocalTime amStartTime) {
        this.amStartTime = amStartTime;
    }

    public LocalTime getAmEndTime() {
        return amEndTime;
    }

    public void setAmEndTime(LocalTime amEndTime) {
        this.amEndTime = amEndTime;
    }

    public LocalTime getPmStartTime() {
        return pmStartTime;
    }

    public void setPmStartTime(LocalTime pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    public LocalTime getPmEndTime() {
        return pmEndTime;
    }

    public void setPmEndTime(LocalTime pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
