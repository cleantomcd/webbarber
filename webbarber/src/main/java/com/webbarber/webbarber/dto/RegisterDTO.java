package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterDTO(@Pattern(regexp = "^.{3,}$", message = "Seu nome deve ter pelo menos 3 caracteres") String name,
                          @Pattern(regexp = "\\d{2}\\d{8,9}", message = "Número de telefone deve estar no formato +55 seguido do DDD e número")
                          String tel,
                          String password) {
}
