package com.webbarber.webbarber.entity;


import jakarta.persistence.*;

@Entity(name = "service")
@Table(name = "service")
public class Service {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int estimatedTime;

    public Service(String name, String description, int estimatedTime) {
        this.name = name;
        this.description = description;
        this.estimatedTime = estimatedTime;
    }

    public Service(String name, int estimatedTime) {
        this.name = name;
        this.estimatedTime = estimatedTime;
    }

    public Service() {
    }

    public Long getId() {
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


}
