package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.exception.TimeSlotNotAvailableException;
import com.webbarber.webbarber.exception.UserAlreadyExistsException;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> newBooking(@RequestBody BookingDTO bookingData) {
        bookingService.bookAppointment(bookingData);
        return ResponseEntity.ok("Horário reservado com sucesso.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingInfoDTO>> getAllSchedules(@RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getAllSchedules(date));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<BookingDTO>> getAllSchedulesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getAllSchedulesByUser(userId));
    }

    @DeleteMapping("/{bookingId}/cancel")
    @Transactional
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
        bookingService.cancelAppointment(bookingId);
        return ResponseEntity.ok("Agendamento cancelado com sucesso.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TimeSlotNotAvailableException.class)
    public ResponseEntity<String> handleTimeSlotNotAvailableException(TimeSlotNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
