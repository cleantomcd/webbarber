package com.webbarber.webbarber.dto;

/**
 * DTO utilizado para representar as informações de um usuário do sistema.
 *
 * @param name Nome do usuário.
 * @param phone Número de telefone do usuário, no formato +55 seguido do DDD e número.
 * @param amountBookedServices Quantidade de serviços agendados pelo usuário.
 */
public record UserInfoDTO(
        String name,
        String phone,
        int amountBookedServices) {
}
