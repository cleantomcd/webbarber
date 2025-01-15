package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.service.TimeSlotAvailabilityService;
import com.webbarber.webbarber.service.TimeSlotService;
import jakarta.transaction.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<String> addClosedSlots(@PathVariable LocalDate date, @RequestBody List<String> slots) {
        System.out.println("log");
        timeSlotService.addClosedSlots(date, slots);
        return ResponseEntity.ok("Horários fechados com sucesso.");
    }

    @PutMapping("/edit/{date}/closed-slots/remove")
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

    @PutMapping("/edit/{date}/close")
    public ResponseEntity<String> closeDate(@PathVariable LocalDate date) {
        timeSlotService.closeDate(date);
        return ResponseEntity.ok("Data marcada como 'fechada'.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlots(date));
    }

    @GetMapping("/all/{time}")
    public ResponseEntity<List<LocalDate>> getDatesWithSpecificTime(@PathVariable LocalTime time) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getDatesWithSpecificTime(time));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<LocalTime>> getTimeSlotsByService(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                 @PathVariable String serviceId) {
        return ResponseEntity.ok(timeSlotAvailabilityService.getAvailableTimeSlotsByService(date, serviceId));
    }

}