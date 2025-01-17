package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.exception.*;
import com.webbarber.webbarber.service.TimeSlotAvailabilityService;
import com.webbarber.webbarber.service.TimeSlotService;
import jakarta.transaction.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;

    public TimeSlotController(TimeSlotService timeSlotService, TimeSlotAvailabilityService timeSlotAvailabilityService) {
        this.timeSlotService = timeSlotService;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
    }

    @PostMapping("/config")
    public ResponseEntity<String> setTimeSlot(@RequestBody StandardTimeSlotDTO standardTimeSlotDTO) {
        timeSlotService.setTimeSlot(standardTimeSlotDTO);
        return ResponseEntity.ok("Horários definidos com sucesso.");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editTimeSlot(@RequestBody EditedTimeSlotDTO editedTimeSlotDTO) {
        timeSlotService.editTimeSlot(editedTimeSlotDTO);
        return ResponseEntity.ok("Horários atualizados para a data " + editedTimeSlotDTO.date());
    }

    @DeleteMapping("/edit/{date}/delete")
    public ResponseEntity<String> deleteTimeSlot(@PathVariable LocalDate date) {
        timeSlotService.removeTimeSlotOverride(date);
        return ResponseEntity.ok("Data atualizada para o horário padrão.");
    }

    @PutMapping("/edit/{date}/closed-slots/add")
    @Transactional
    public ResponseEntity<String> addClosedSlots(@PathVariable LocalDate date, @RequestBody List<String> slots) {
        timeSlotService.addClosedSlots(date, slots);
        return ResponseEntity.ok("Horários fechados com sucesso.");
    }

    @PutMapping("/edit/{date}/closed-slots/remove")
    @Transactional
    public ResponseEntity<String> removeClosedSlots(@PathVariable LocalDate date, @RequestBody List<String> slots) {
        timeSlotService.removeClosedSlots(date, slots);
        return ResponseEntity.ok("Horários fechados removidos com sucesso.");
    }

    @PutMapping("/edit/{date}/closed-slots/clear")
    @Transactional
    public ResponseEntity<String> clearClosedSlots(@PathVariable LocalDate date) {
        timeSlotService.clearClosedSlots(date);
        return ResponseEntity.ok("Horários fechados deletados com sucesso.");
    }

    @PutMapping("/edit/{date}/{isOpen}")
    @Transactional
    public ResponseEntity<String> setDataAvailability(@PathVariable LocalDate date, @PathVariable boolean isOpen) {
        timeSlotService.setDataAvailability(date, isOpen);
        return ResponseEntity.ok("Disponibilidade da data alterada com sucesso");
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocalTime>> getAllTimeSlots(@RequestParam("date") @DateTimeFormat
            (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(date, null));
    }

    @GetMapping("/all/{serviceId}")
    public ResponseEntity<List<LocalTime>> getTimeSlotsByService(@RequestParam("date") @DateTimeFormat
            (iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String serviceId) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(date, serviceId));
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