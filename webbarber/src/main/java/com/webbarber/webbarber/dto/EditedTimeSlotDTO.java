package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO utilizado para representar alterações nos horários de atendimento de um barbeiro
 * para um dia específico.
 *
 * @param date        Data específica para a qual os horários serão editados.
 * @param amStartTime Horário de início do atendimento matutino.
 * @param amEndTime   Horário de término do atendimento matutino.
 * @param pmStartTime Horário de início do atendimento vespertino.
 * @param pmEndTime   Horário de término do atendimento vespertino.
 * @param interval    Intervalo entre os horários disponíveis para agendamento, geralmente em minutos.
 * @param closedSlots Lista de horários específicos que estarão fechados para agendamento.
 * @param isClosed    Indica se o dia inteiro está fechado para atendimento.
 */
public record EditedTimeSlotDTO(
        LocalDate date,
        LocalTime amStartTime,
        LocalTime amEndTime,
        LocalTime pmStartTime,
        LocalTime pmEndTime,
        int interval,
        List<String> closedSlots,
        boolean isClosed
) {}
