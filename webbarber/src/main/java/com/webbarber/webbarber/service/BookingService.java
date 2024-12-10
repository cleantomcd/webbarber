package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void bookAppointment(BookingDTO data) {
        Booking booking = new Booking(data);
        bookingRepository.save(booking);
    }

    public void cancelAppointment(BookingDTO data) {
        Booking booking = new Booking(data);
        this.bookingRepository.delete(booking);
    }
}
