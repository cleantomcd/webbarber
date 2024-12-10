package com.webbarber.webbarber.dto;

public record ServiceDTO(String name, String description, int estimatedTime, int priceInCents, boolean active) {
}
