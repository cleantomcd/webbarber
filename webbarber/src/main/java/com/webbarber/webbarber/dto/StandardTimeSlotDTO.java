package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;

public record StandardTimeSlotDTO(
        @Min(1) @Max(7)
        int dayOfWeek,
        LocalTime amStartTime,
        LocalTime amEndTime,
        LocalTime pmStartTime,
        LocalTime pmEndTime,
        int interval) {
}
