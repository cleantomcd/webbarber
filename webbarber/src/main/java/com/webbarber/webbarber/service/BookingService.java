package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ServiceService serviceService;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;

    public BookingService(BookingRepository bookingRepository,
                          UserService userService, ServiceService serviceService,
                          TimeSlotAvailabilityService timeSlotAvailabilityService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.serviceService = serviceService;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
    }

    public void bookAppointment(BookingDTO data) {
        validateUser(data.userId());
        validateService(data.serviceId());
        validateAvailability(data.date(), data.startTime(), data.serviceId());
        Booking booking = new Booking(data);
        bookingRepository.save(booking);
    }

    private void validateUser(String userId) {
        if (!userService.existsUserById(userId)) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }

    private void validateService(String serviceId) {
        if (!serviceService.existsById(serviceId)) {
            throw new IllegalArgumentException("Serviço não encontrado.");
        }
    }

    private void validateAvailability(LocalDate date, LocalTime startTime, String serviceId) {
        if (timeSlotAvailabilityService.isBookingAvailable(date, startTime, serviceId)) {
            throw new IllegalArgumentException("Horário não disponível.");
        }
    }

    public void cancelAppointment(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        optionalBooking.ifPresent(bookingRepository::delete);
    }




}
