package com.webbarber.webbarber.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public record BookingDTO(String userId, String barberId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
    public BookingDTO(String userId, String barberId, String serviceId, LocalDate date, LocalTime startTime) {
        this(userId, barberId, serviceId, date, startTime, null);
    }
}
