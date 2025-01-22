package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingInfoDTO(String userName, String barberName, String serviceName, LocalDate date, LocalTime startTime, LocalTime endTime) {

}
