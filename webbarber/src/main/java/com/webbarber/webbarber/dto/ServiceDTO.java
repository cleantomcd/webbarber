package com.webbarber.webbarber.dto;

/**
 * DTO utilizado para representar as informações de um serviço oferecido por um barbeiro.
 *
 * @param name Nome do serviço (ex: Corte de cabelo, Barba, etc).
 * @param description Descrição detalhada do serviço.
 * @param duration Duração do serviço em minutos.
 * @param priceInCents Preço do serviço em centavos (ex: 1500 para R$15,00).
 * @param active Indica se o serviço está ativo e disponível para agendamento.
 */
public record ServiceDTO(
        String name,
        String description,
        int duration,
        int priceInCents,
        boolean active) {
}
