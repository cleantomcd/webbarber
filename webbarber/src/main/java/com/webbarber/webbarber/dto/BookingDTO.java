package com.webbarber.webbarber.dto;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.entity.Service;
import java.time.LocalTime;

public record BookingDTO(String userId, String serviceId, LocalTime start, LocalTime estimatedEnd) {
}
