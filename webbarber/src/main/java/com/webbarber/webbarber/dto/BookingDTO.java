package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO utilizado para representar uma reserva (agendamento) no sistema.
 *
 * @param userId    Identificador do usuário que realizou a reserva.
 * @param barberId  Identificador do barbeiro responsável pelo atendimento.
 * @param serviceId Identificador do serviço que será realizado na reserva.
 * @param date      Data do agendamento.
 * @param startTime Horário de início do agendamento.
 * @param endTime   Horário de término do agendamento (opcional, pode ser nulo).
 */
public record BookingDTO(String userId, String barberId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {

    /**
     * Construtor alternativo que permite criar um agendamento sem especificar o horário de término.
     *
     * @param userId    Identificador do usuário.
     * @param barberId  Identificador do barbeiro.
     * @param serviceId Identificador do serviço.
     * @param date      Data do agendamento.
     * @param startTime Horário de início do agendamento.
     */
    public BookingDTO(String userId, String barberId, String serviceId, LocalDate date, LocalTime startTime) {
        this(userId, barberId, serviceId, date, startTime, null);
    }
}
