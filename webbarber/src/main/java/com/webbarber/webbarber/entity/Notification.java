package com.webbarber.webbarber.entity;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Client client;
    private String message;
    private String type;
    private String status;

    public Notification(Client client, String message, String type, String status) {
        this.client = client;
        this.message = message;
        this.type = type;
        this.status = status;
    }

    public Notification() {}

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
