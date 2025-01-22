package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.Pattern;

public record AuthenticationDTO(
        @Pattern(regexp = "\\d{2}\\d{8,9}", message = "Invalid phone number format")
        String phone,
        String password) {}
