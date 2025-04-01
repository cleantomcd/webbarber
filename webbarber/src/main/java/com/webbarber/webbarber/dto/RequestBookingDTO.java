package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO utilizado para representar a solicitação de agendamento de um serviço com um barbeiro.
 *
 * @param barberId Identificador do barbeiro para quem o serviço será agendado.
 * @param serviceId Identificador do serviço que será prestado.
 * @param date Data do agendamento.
 * @param startTime Hora de início do serviço.
 * @param endTime Hora de término do serviço.
 */
public record RequestBookingDTO(
        String barberId,
        String serviceId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime) {
}
