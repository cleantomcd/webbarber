package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.exception.*;
import com.webbarber.webbarber.service.BarberService;
import com.webbarber.webbarber.service.TimeSlotAvailabilityService;
import com.webbarber.webbarber.service.TimeSlotService;
import jakarta.transaction.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Controlador responsável pela gestão dos horários disponíveis para agendamentos dos barbeiros.
 */
@RestController
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;
    private final BarberService barberService;

    /**
     * Construtor da classe {@code TimeSlotController}.
     *
     * @param timeSlotService               Serviço para gerenciar os horários.
     * @param timeSlotAvailabilityService   Serviço para verificar disponibilidade de horários.
     * @param barberService                 Serviço para gerenciar informações dos barbeiros.
     */
    public TimeSlotController(TimeSlotService timeSlotService, TimeSlotAvailabilityService timeSlotAvailabilityService, BarberService barberService) {
        this.timeSlotService = timeSlotService;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
        this.barberService = barberService;
    }

    /**
     * Define os horários padrão de atendimento de um barbeiro.
     *
     * @param authentication       Autenticação do barbeiro logado.
     * @param standardTimeSlotDTO  DTO contendo as configurações de horários.
     * @return Resposta indicando sucesso na configuração dos horários.
     */
    @PostMapping("/barber/schedules/config")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> setTimeSlot(Authentication authentication, @RequestBody StandardTimeSlotDTO standardTimeSlotDTO) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.setTimeSlot(barberId, standardTimeSlotDTO);
        return ResponseEntity.ok("Horários definidos com sucesso.");
    }

    /**
     * Edita os horários disponíveis para uma data específica.
     *
     * @param authentication     Autenticação do barbeiro logado.
     * @param editedTimeSlotDTO  DTO contendo os horários editados.
     * @return Resposta indicando sucesso na edição dos horários.
     */
    @PostMapping("/barber/schedules/edit")
    public ResponseEntity<String> editTimeSlot(Authentication authentication, @RequestBody EditedTimeSlotDTO editedTimeSlotDTO) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.editTimeSlot(barberId, editedTimeSlotDTO);
        return ResponseEntity.ok("Horários atualizados para a data " + editedTimeSlotDTO.date());
    }

    /**
     * Remove a configuração personalizada de horários para uma data, retornando ao padrão.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data da configuração a ser removida.
     * @return Resposta indicando sucesso na remoção.
     */
    @DeleteMapping("/barber/schedules/edit/{date}/delete")
    public ResponseEntity<String> deleteTimeSlot(Authentication authentication, @PathVariable LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.removeTimeSlotOverride(barberId, date);
        return ResponseEntity.ok("Data atualizada para o horário padrão.");
    }

    /**
     * Adiciona horários fechados para uma determinada data.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data de referência.
     * @param slots           Lista de horários a serem fechados.
     * @return Resposta indicando sucesso na operação.
     */
    @PutMapping("/barber/schedules/edit/{date}/closed-slots/add")
    @Transactional
    public ResponseEntity<String> addClosedSlots(Authentication authentication, @PathVariable LocalDate date, @RequestBody List<String> slots) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.addClosedSlots(barberId, date, slots);
        return ResponseEntity.ok("Horários fechados com sucesso.");
    }

    /**
     * Remove horários fechados de uma determinada data.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data de referência.
     * @param slots           Lista de horários a serem reabertos.
     * @return Resposta indicando sucesso na remoção dos horários fechados.
     */
    @PutMapping("/barber/schedules/edit/{date}/closed-slots/remove")
    @Transactional
    public ResponseEntity<String> removeClosedSlots(Authentication authentication, @PathVariable LocalDate date, @RequestBody List<String> slots) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.removeClosedSlots(barberId, date, slots);
        return ResponseEntity.ok("Horários fechados removidos com sucesso.");
    }

    /**
     * Limpa todos os horários fechados de uma data.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data de referência.
     * @return Resposta indicando sucesso na operação.
     */
    @PutMapping("/barber/schedules/edit/{date}/closed-slots/clear")
    @Transactional
    public ResponseEntity<String> clearClosedSlots(Authentication authentication, @PathVariable LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.clearClosedSlots(barberId, date);
        return ResponseEntity.ok("Horários fechados deletados com sucesso.");
    }

    /**
     * Define a disponibilidade de uma data específica.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data a ser alterada.
     * @param isOpen          Disponibilidade (true para aberto, false para fechado).
     * @return Resposta indicando sucesso na alteração da disponibilidade.
     */
    @PutMapping("/barber/schedules/edit/{date}/{isOpen}")
    @Transactional
    public ResponseEntity<String> setDataAvailability(Authentication authentication, @PathVariable LocalDate date, @PathVariable boolean isOpen) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.setDataAvailability(barberId, date, isOpen);
        return ResponseEntity.ok("Disponibilidade da data alterada com sucesso.");
    }

    /**
     * Obtém todos os horários disponíveis para uma data específica.
     *
     * @param authentication  Autenticação do barbeiro logado.
     * @param date            Data de referência.
     * @return Lista de horários disponíveis.
     */
    @GetMapping("/barber/schedules/all")
    public ResponseEntity<List<LocalTime>> getAllTimeSlots(Authentication authentication, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(barberId, date, null));
    }

    /**
     * Obtém os horários disponíveis para um serviço específico.
     *
     * @param barberId   ID do barbeiro.
     * @param date       Data de referência.
     * @param serviceId  ID do serviço.
     * @return Lista de horários disponíveis.
     */
    @GetMapping("/{barberId}/all/{serviceId}")
    public ResponseEntity<List<LocalTime>> getTimeSlotsByService(@PathVariable String barberId, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String serviceId) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(barberId, date, serviceId));
    }
}
