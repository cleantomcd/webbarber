package com.webbarber.webbarber.dto;

/**
 * DTO utilizado para representar a resposta de autenticação após um login bem-sucedido.
 *
 * @param token Token JWT gerado para autenticação do usuário.
 */
public record LoginResponseDTO(String token) {
}
