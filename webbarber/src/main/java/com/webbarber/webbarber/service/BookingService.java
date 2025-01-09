package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void bookAppointment(BookingDTO data) {
        if(!isBookingAvailable(data.date(), data.startTime())) throw new IllegalArgumentException("Horário já reservado.");
        Booking booking = new Booking(data);
        bookingRepository.save(booking);
    }

    public void cancelAppointment(BookingDTO data) {
        Booking booking = new Booking(data);
        this.bookingRepository.delete(booking);
    }

    public boolean isBookingAvailable(LocalDate date, LocalTime startTime) {
        return bookingRepository.findByDateAndStartTime(date, startTime).isEmpty();
    }
}
