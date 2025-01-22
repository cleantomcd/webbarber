package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.dto.RequestBookingDTO;
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

    public void bookAppointment(String userId, RequestBookingDTO data) {
        validateUser(userId);
        validateService(data.barberId(), data.serviceId());
        validateAvailability(data.barberId(), data.date(), data.startTime(), data.serviceId());
        Booking booking = createBooking(userId, data.barberId(), data);
        bookingRepository.save(booking);
    }

    public List<BookingInfoDTO> getAllSchedules(LocalDate date) {
        return bookingRepository.findAllByBarberIdAndDate(null, date);
    }

    public Booking createBooking(String userId, String barberId, RequestBookingDTO data) {
        int serviceDuration = serviceService.getDurationById(null, data.serviceId());
        int interval = timeSlotAvailabilityService.getInterval(barberId, data.date());
        LocalTime endTime = data.startTime().plusMinutes(interval * serviceDuration);

        return new Booking(userId, data, endTime);
    }

    private void validateUser(String userId) {
        if (!userService.existsUserById(userId)) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }
    }

    private void validateBarber(String barberId) {

    }

    private void validateService(String barberId, String serviceId) {
        if (!serviceService.existsByBarberIdAndId(barberId, serviceId)) {
            throw new ServiceNotFoundException("Serviço não encontrado.");
        }
    }

    private void validateAvailability(String barberId, LocalDate date, LocalTime startTime, String serviceId) {
        if (!timeSlotAvailabilityService.isBkAvailable(barberId, date, startTime, serviceId)) {
            throw new TimeSlotNotAvailableException("Horário não disponível.");
        }
    }

    public void cancelAppointment(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty()) throw new BookingNotFoundException("Agendamento não encontrado.");
        bookingRepository.delete(optionalBooking.get());
    }

    public List<BookingDTO> getAllSchedulesByUser(String userId) {
        return bookingRepository.findAllByBarberIdAndUserId(null, userId);
    }

    public String getUserIdByPhone(String phone) {
        return userService.findIdByPhone(phone);
    }




}
