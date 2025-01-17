package com.webbarber.webbarber;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.webbarber.webbarber.controller.BookingController;
import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.repository.BookingRepository;
import com.webbarber.webbarber.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.time.LocalTime;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository; // Mock do repositório

    @InjectMocks
    private BookingService bookingService; // Classe que estamos testando

    public BookingServiceTest() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testBookingConflict() {
        // Dados do primeiro agendamento (já existente no "banco")
        Booking existingBooking = new Booking(
                 // ID do Booking
                "userId", // userId
                "serviceId", // serviceId
                LocalDate.now(), // date
                LocalTime.of(10, 0), // startTime
                LocalTime.of(11, 0) // endTime
        );

        // Dados do segundo agendamento (conflitante)
        BookingDTO conflictingBooking = new BookingDTO(
                "userId", // userId
                "ServiceId", // serviceId
                LocalDate.now(), // date
                LocalTime.of(10, 30) // startTime
        );

        // Configurando o mock para retornar o booking existente
        when(bookingRepository.findConflictingBooking(LocalDate.now(), LocalTime.of(10, 30)));

        // Execução e validação do conflito
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.bookAppointment(conflictingBooking)
        );

        // Validar a mensagem da exceção
        assertEquals("Horário indisponível.", exception.getMessage());

    }
}

