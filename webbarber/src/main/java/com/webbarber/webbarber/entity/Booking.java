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

    public Booking(BookingDTO data, LocalTime endTime) {
        this.userId = data.userId();
        this.serviceId = data.serviceId();
        this.startTime = data.startTime();
        this.endTime = endTime;
        this.date = data.date();
    }

    public Booking(String userId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime  ) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.date = date;
        this.serviceId = serviceId;
        this.userId = userId;
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

