package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.BookingDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "booking")
@Table(name = "booking")
public class Booking {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String serviceId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    public Booking() {}

    public Booking(BookingDTO data) {
        this.userId = data.userId();
        this.serviceId = data.serviceId();
        this.startTime = data.startTime();
        this.endTime = data.endTime();
        this.date = data.date();
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

}

