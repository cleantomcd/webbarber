package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.dto.RequestBookingDTO;
import com.webbarber.webbarber.exception.*;
import com.webbarber.webbarber.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador responsável pelo gerenciamento de agendamentos.
 */
@RestController
public class BookingController {
    private final BookingService bookingService;

    /**
     * Construtor da classe BookingController.
     *
     * @param bookingService Serviço de agendamento utilizado pelo controlador.
     */
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Cria um novo agendamento.
     *
     * @param authentication Informações do usuário autenticado.
     * @param bookingData    Dados do agendamento a ser criado.
     * @return Mensagem de confirmação de agendamento.
     */
    @PostMapping("/book")
    public ResponseEntity<String> newBooking(Authentication authentication, @RequestBody RequestBookingDTO bookingData) {
        String userId = bookingService.getUserIdByPhone(authentication.getName());
        bookingService.bookAppointment(userId, bookingData);
        return ResponseEntity.ok("Horário reservado com sucesso.");
    }

    /**
     * Obtém todos os agendamentos para uma determinada data.
     *
     * @param date Data dos agendamentos.
     * @return Lista de agendamentos.
     */
    @GetMapping("/schedules/my-schedules/all")
    public ResponseEntity<List<BookingInfoDTO>> getAllSchedules(@RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getAllSchedules(date));
    }

    /**
     * Obtém todos os agendamentos de um usuário específico.
     *
     * @param userId ID do usuário.
     * @return Lista de agendamentos do usuário.
     */
    @GetMapping("/barber/schedules/all/{userId}")
    public ResponseEntity<List<BookingDTO>> getAllSchedulesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getAllSchedulesByUser(userId));
    }

    /**
     * Cancela um agendamento existente.
     *
     * @param bookingId ID do agendamento a ser cancelado.
     * @return Mensagem de confirmação de cancelamento.
     */
    @DeleteMapping("/barber/schedules/{bookingId}/cancel")
    @Transactional
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
        bookingService.cancelAppointment(bookingId);
        return ResponseEntity.ok("Agendamento cancelado com sucesso.");
    }

    /**
     * Manipula exceções quando um usuário não é encontrado.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com status 404 e mensagem de erro.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Manipula exceções quando um serviço não é encontrado.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com status 404 e mensagem de erro.
     */
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Manipula exceções quando o horário desejado não está disponível.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com status 409 e mensagem de erro.
     */
    @ExceptionHandler(TimeSlotNotAvailableException.class)
    public ResponseEntity<String> handleTimeSlotNotAvailableException(TimeSlotNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Manipula exceções quando um agendamento não é encontrado.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com status 404 e mensagem de erro.
     */
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<String> handleBookingNotFoundException(BookingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
