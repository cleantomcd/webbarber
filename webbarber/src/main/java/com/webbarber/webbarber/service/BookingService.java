package com.webbarber.webbarber.service;

import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking bookAppointment(Booking booking) {
       return this.bookingRepository.save(booking);
    }

    public void cancelAppointment(Booking booking) {
        this.bookingRepository.delete(booking);
    }
}
