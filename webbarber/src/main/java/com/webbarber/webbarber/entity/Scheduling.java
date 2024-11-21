package com.webbarber.webbarber.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity(name = "scheduling")
@Table(name = "scheduling")
public class Scheduling {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Service service;

    private LocalTime start;
    private LocalTime estimatedEnd;

    public Scheduling(User user, Service service, LocalTime start, LocalTime estimatedEnd) {
        this.user = user;
        this.service = service;
        this.start = start;
        this.estimatedEnd = estimatedEnd;
    }

    public Scheduling() {}

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEstimatedEnd() {
        return estimatedEnd;
    }

}

