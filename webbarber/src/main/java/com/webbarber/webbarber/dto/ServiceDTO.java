package com.webbarber.webbarber.dto;

public record ServiceDTO(String name, String description, int duration, int priceInCents, boolean active) {
}
