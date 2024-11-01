package com.webbarber.webbarber.dto;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.entity.Service;
import java.time.LocalTime;

public record SchedulingDTO(User user, Service service, LocalTime start, LocalTime estimatedEnd) {
}
