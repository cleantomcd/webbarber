package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.Pattern;

/**
 * DTO utilizado para representar os dados necessários para o registro de um novo usuário
 * no sistema.
 *
 * @param name  Nome completo do usuário, com pelo menos 3 caracteres.
 * @param phone Número de telefone do usuário, no formato +55 seguido do DDD e número (exemplo: +5511998765432).
 * @param password Senha do usuário para autenticação no sistema.
 */
public record RegisterDTO(
        @Pattern(regexp = "^.{3,}$", message = "Seu nome deve ter pelo menos 3 caracteres") String name,
        @Pattern(regexp = "\\d{2}\\d{8,9}", message = "Número de telefone deve estar no formato +55 seguido do DDD e número")
        String phone,
        String password) {
}
