package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record RequestBookingDTO(String barberId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
}
