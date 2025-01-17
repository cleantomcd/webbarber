package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.exception.BookingNotFoundException;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.exception.TimeSlotNotAvailableException;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
        Booking booking = createBooking(data);
        bookingRepository.save(booking);
    }

    public List<BookingInfoDTO> getAllSchedules(LocalDate date) {
        return bookingRepository.findAllByDate(date);
    }

    public Booking createBooking(BookingDTO data) {
        int serviceDuration = serviceService.getDurationById(data.serviceId());
        int interval = timeSlotAvailabilityService.getInterval(data.date());
        LocalTime endTime = data.startTime().plusMinutes(interval * serviceDuration);

        return new Booking(data, endTime);
    }

    private void validateUser(String userId) {
        if (!userService.existsUserById(userId)) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }
    }

    private void validateService(String serviceId) {
        if (!serviceService.existsById(serviceId)) {
            throw new ServiceNotFoundException("Serviço não encontrado.");
        }
    }

    private void validateAvailability(LocalDate date, LocalTime startTime, String serviceId) {
        if (!timeSlotAvailabilityService.isBkAvailable(date, startTime, serviceId)) {
            throw new TimeSlotNotAvailableException("Horário não disponível.");
        }
    }

    public void cancelAppointment(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty()) throw new BookingNotFoundException("Agendamento não encontrado.");
        bookingRepository.delete(optionalBooking.get());
    }

    public List<BookingDTO> getAllSchedulesByUser(String userId) {
        return bookingRepository.findAllByUserId(userId);
    }




}
