package com.webbarber.webbarber.entity;


import com.webbarber.webbarber.dto.ServiceDTO;
import jakarta.persistence.*;

@Entity(name = "services")
@Table(name = "services")
public class Service {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String description;
    private int estimatedTime;
    private int priceInCents;
    private boolean active;

    public Service(String name, String description, int estimatedTime, int priceInCents) {
        this.name = name;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    public Service(String name, int estimatedTime, int priceInCents) {
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    public Service(ServiceDTO serviceDTO) {
        this.name = serviceDTO.name();
        this.description = serviceDTO.description();
        this.estimatedTime = serviceDTO.estimatedTime();
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

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setPriceInCents(int priceInCents) { this.priceInCents = priceInCents; }

    public int getPriceInCents() { return this.priceInCents; }

    public boolean isActive() { return this.active; }

    public void setActive(boolean status) {
        this.active = status;
    }



}
