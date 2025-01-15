package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "timeslot_override")
@Entity(name = "timeslot_override")
public class TimeSlotOverride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate date;
    private LocalTime amStartTime;
    private LocalTime amEndTime;
    private LocalTime pmStartTime;
    private LocalTime pmEndTime;
    private int interval;

    @ElementCollection
    private List<String> closedSlots;

    private boolean isClosed;

    public TimeSlotOverride() {

    }

    public TimeSlotOverride(EditedTimeSlotDTO editedTimeSlotDTO) {
        this.date = editedTimeSlotDTO.date();
        this.amStartTime = editedTimeSlotDTO.amStartTime();
        this.amEndTime = editedTimeSlotDTO.amEndTime();
        this.pmStartTime = editedTimeSlotDTO.pmStartTime();
        this.pmEndTime = editedTimeSlotDTO.pmEndTime();
        this.interval = editedTimeSlotDTO.interval();
        this.isClosed = editedTimeSlotDTO.isClosed();
        this.closedSlots = editedTimeSlotDTO.closedSlots();
    }

    public TimeSlotOverride(LocalDate date) {
        this.date = date;
        this.amStartTime = null;
        this.amEndTime = null;
        this.pmStartTime = null;
        this.pmEndTime = null;
        this.interval = 0;
        this.closedSlots = null;
        this.isClosed = true;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getAmStartTime() {
        return amStartTime;
    }

    public LocalTime getAmEndTime() {
        return amEndTime;
    }

    public LocalTime getPmStartTime() {
        return pmStartTime;
    }

    public LocalTime getPmEndTime() {
        return pmEndTime;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmStartTime(LocalTime amStartTime) {
        this.amStartTime = amStartTime;
    }

    public void setAmEndTime(LocalTime amEndTime) {
        this.amEndTime = amEndTime;
    }

    public void setPmEndTime(LocalTime pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    public void setPmStartTime(LocalTime pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<String> getClosedSlots() {
        return closedSlots;
    }

    public void setClosedSlots(List<String> closedSlots) {
        this.closedSlots = closedSlots;
    }

    public List<LocalTime> getParsedClosedSlots() {
        return this.closedSlots.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    public void addClosedSlots(List<String> slots) {
        this.closedSlots.addAll(slots);
    }

    public void removeClosedSlots(List<String> slots) {
        this.closedSlots.removeAll(slots);
    }

    public void clearClosedSlots() {
        this.closedSlots.clear();
    }

}
