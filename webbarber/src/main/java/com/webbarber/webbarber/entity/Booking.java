package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.RequestBookingDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "Booking")
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private String id;

    private String barberId;

    private String userId;
    private String serviceId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Booking() {}

    public Booking(String userId, RequestBookingDTO data, LocalTime endTime) {
        this.barberId = data.barberId();
        this.userId = userId;
        this.serviceId = data.serviceId();
        this.startTime = data.startTime();
        this.endTime = endTime;
        this.date = data.date();
    }

    public Booking(String barberId, String userId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime  ) {
        this.barberId = barberId;
        this.endTime = endTime;
        this.startTime = startTime;
        this.date = date;
        this.serviceId = serviceId;
        this.userId = userId;
    }

    public String getBarberId() {
        return barberId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void addStartTime(int minutes) {
        this.startTime = this.startTime.plusMinutes(minutes);
    }

}

