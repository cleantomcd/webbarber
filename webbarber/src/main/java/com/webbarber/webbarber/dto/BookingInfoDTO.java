package com.webbarber.webbarber.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO utilizado para representar as informações detalhadas de um agendamento.
 *
 * @param userName    Nome do usuário que realizou a reserva.
 * @param barberName  Nome do barbeiro responsável pelo atendimento.
 * @param serviceName Nome do serviço agendado.
 * @param date        Data do agendamento.
 * @param startTime   Horário de início do agendamento.
 * @param endTime     Horário de término do agendamento.
 */
public record BookingInfoDTO(String userName, String barberName, String serviceName, LocalDate date, LocalTime startTime, LocalTime endTime) {

}
