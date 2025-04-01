package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.Pattern;

/**
 * DTO utilizado para autenticação de usuários.
 * Contém os dados necessários para o login no sistema.
 *
 * @param phone    Número de telefone do usuário, que deve seguir o formato de dois dígitos para o DDD
 *                 seguido por um número de telefone com 8 ou 9 dígitos.
 * @param password Senha do usuário.
 */
public record AuthenticationDTO(
        @Pattern(regexp = "\\d{2}\\d{8,9}", message = "Invalid phone number format")
        String phone,
        String password) {}
