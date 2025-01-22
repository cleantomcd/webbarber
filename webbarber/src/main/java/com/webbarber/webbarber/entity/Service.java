package com.webbarber.webbarber.entity;


import com.webbarber.webbarber.dto.ServiceDTO;
import jakarta.persistence.*;

@Entity(name = "Service")
@Table(name = "services")
public class Service {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String barberId;

    private String name;
    private String description;
    private int duration;
    private int priceInCents;
    private boolean active;

    public Service(String barberId, String name, String description, int duration, int priceInCents) {
        this.barberId = barberId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    public Service(String barberId, String name, int duration, int priceInCents) {
        this.barberId = barberId;
        this.name = name;
        this.duration = duration;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    public String getBarberId() {
        return barberId;
    }

    public Service(String barberId, ServiceDTO serviceDTO) {
        this.barberId = barberId;
        this.name = serviceDTO.name();
        this.description = serviceDTO.description();
        this.duration = serviceDTO.duration();
        this.priceInCents = serviceDTO.priceInCents();
        this.active = true;
    }

    public Service() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPriceInCents(int priceInCents) { this.priceInCents = priceInCents; }

    public int getPriceInCents() { return this.priceInCents; }

    public boolean isActive() { return this.active; }

    public void setActive(boolean status) {
        this.active = status;
    }

}
