package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.BookingDTO;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity(name = "scheduling")
@Table(name = "scheduling")
public class Booking {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String serviceId;
    private LocalTime start;
    private LocalTime estimatedEnd;

    public Booking(String userId, String serviceId, LocalTime start, LocalTime estimatedEnd) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.start = start;
        this.estimatedEnd = estimatedEnd;
    }

    public Booking() {}

    public Booking(BookingDTO data) {
        this.userId = data.userId();
        this.serviceId = data.serviceId();
        this.start = data.start();
        this.estimatedEnd = data.estimatedEnd();
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

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEstimatedEnd() {
        return estimatedEnd;
    }

}

