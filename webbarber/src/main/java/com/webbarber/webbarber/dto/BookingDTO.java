package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingDTO(String userId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
}
