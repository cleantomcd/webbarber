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

@RestController
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;
    private final BarberService barberService;

    public TimeSlotController(TimeSlotService timeSlotService, TimeSlotAvailabilityService timeSlotAvailabilityService, BarberService barberService, BarberService barberService1) {
        this.timeSlotService = timeSlotService;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
        this.barberService = barberService1;
    }

    @PostMapping("/barber/schedules/config")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> setTimeSlot(Authentication authentication, @RequestBody StandardTimeSlotDTO standardTimeSlotDTO) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.setTimeSlot(barberId, standardTimeSlotDTO);
        return ResponseEntity.ok("Horários definidos com sucesso.");
    }

    @PostMapping("/barber/schedules/edit")
    public ResponseEntity<String> editTimeSlot(Authentication authentication, @RequestBody EditedTimeSlotDTO editedTimeSlotDTO) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.editTimeSlot(barberId, editedTimeSlotDTO);
        return ResponseEntity.ok("Horários atualizados para a data " + editedTimeSlotDTO.date());
    }

    @DeleteMapping("/barber/schedules/edit/{date}/delete")
    public ResponseEntity<String> deleteTimeSlot(Authentication authentication, @PathVariable LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.removeTimeSlotOverride(barberId, date);
        return ResponseEntity.ok("Data atualizada para o horário padrão.");
    }

    @PutMapping("/barber/schedules/edit/{date}/closed-slots/add")
    @Transactional
    public ResponseEntity<String> addClosedSlots(Authentication authentication, @PathVariable LocalDate date, @RequestBody List<String> slots) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.addClosedSlots(barberId, date, slots);
        return ResponseEntity.ok("Horários fechados com sucesso.");
    }

    @PutMapping("/barber/schedules/edit/{date}/closed-slots/remove")
    @Transactional
    public ResponseEntity<String> removeClosedSlots(Authentication authentication, @PathVariable LocalDate date, @RequestBody List<String> slots) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.removeClosedSlots(barberId, date, slots);
        return ResponseEntity.ok("Horários fechados removidos com sucesso.");
    }

    @PutMapping("/barber/schedules/edit/{date}/closed-slots/clear")
    @Transactional
    public ResponseEntity<String> clearClosedSlots(Authentication authentication, @PathVariable LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.clearClosedSlots(barberId, date);
        return ResponseEntity.ok("Horários fechados deletados com sucesso.");
    }

    @PutMapping("/barber/schedules/edit/{date}/{isOpen}")
    @Transactional
    public ResponseEntity<String> setDataAvailability(Authentication authentication, @PathVariable LocalDate date, @PathVariable boolean isOpen) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        timeSlotService.setDataAvailability(barberId, date, isOpen);
        return ResponseEntity.ok("Disponibilidade da data alterada com sucesso");
    }

    @GetMapping("/barber/schedules/all")
    public ResponseEntity<List<LocalTime>> getAllTimeSlots(Authentication authentication, @RequestParam("date") @DateTimeFormat
            (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(barberId, date, null));
    }

    @GetMapping("/{barberId}/all/{serviceId}")
    public ResponseEntity<List<LocalTime>> getTimeSlotsByService(@PathVariable String barberId, @RequestParam("date") @DateTimeFormat
            (iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String serviceId) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(barberId, date, serviceId));
    }

    @ExceptionHandler(TimeSlotNotAvailableException.class)
    public ResponseEntity<String> handleTimeSlotNotAvailableException(TimeSlotNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDayOfWeekException.class)
    public ResponseEntity<String> handleInvalidDayOfWeekException(InvalidDayOfWeekException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTimeIntervalException.class)
    public ResponseEntity<String> handleInvalidTimeIntervalException(InvalidTimeIntervalException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidStartTimeException.class)
    public ResponseEntity<String> handleInvalidStartTimeException(InvalidStartTimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TimeSlotNotFoundException.class)
    public ResponseEntity<String> handleTimeSlotNotFoundException(TimeSlotNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}