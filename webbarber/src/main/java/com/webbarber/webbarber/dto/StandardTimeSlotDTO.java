package com.webbarber.webbarber.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;

/**
 * DTO utilizado para definir os horários padrão de atendimento de um barbeiro.
 *
 * @param dayOfWeek   Dia da semana representado por um número de 1 a 7 (1 = Domingo, 7 = Sábado).
 * @param amStartTime Horário de início do atendimento matutino.
 * @param amEndTime   Horário de término do atendimento matutino.
 * @param pmStartTime Horário de início do atendimento vespertino.
 * @param pmEndTime   Horário de término do atendimento vespertino.
 * @param interval    Intervalo entre os horários disponíveis para agendamento, geralmente em minutos.
 */
public record StandardTimeSlotDTO(
        @Min(1) @Max(7)
        int dayOfWeek,
        LocalTime amStartTime,
        LocalTime amEndTime,
        LocalTime pmStartTime,
        LocalTime pmEndTime,
        int interval) {
}
