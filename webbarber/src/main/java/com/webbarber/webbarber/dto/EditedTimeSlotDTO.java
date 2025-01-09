package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EditedTimeSlotDTO(
        LocalDate date,
        LocalTime amStartTime,
        LocalTime amEndTime,
        LocalTime pmStartTime,
        LocalTime pmEndTime,
        int interval,
        List<String> closedSlots,
        boolean isClosed
) {}



