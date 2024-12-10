package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/schedules")
    public ResponseEntity<Void> getAllAvailableSchedules() {
        /* TODO */
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getBookingServices() {
        /* TODO */
        return ResponseEntity.ok().build();
    }

    @PostMapping("/book")
    public ResponseEntity<Void> newBooking(@RequestBody BookingDTO bookingData) {
        /* TODO */
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        /* TODO */
        return ResponseEntity.ok().build();
    }


}
