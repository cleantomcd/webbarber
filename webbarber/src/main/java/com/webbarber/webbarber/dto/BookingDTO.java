package com.webbarber.webbarber.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public record BookingDTO(String userId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
    public BookingDTO(String userId, String serviceId, LocalDate date, LocalTime startTime) {
        this(userId, serviceId, date, startTime, null);
    }
}
