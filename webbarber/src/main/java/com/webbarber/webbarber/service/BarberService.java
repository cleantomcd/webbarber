package com.webbarber.webbarber.service;

import com.webbarber.webbarber.repository.BarberRepository;
import org.springframework.stereotype.Service;

@Service
public class BarberService {

    private final BarberRepository barberRepository;

    public BarberService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    public boolean existsByPhone(String phone) {
        return barberRepository.existsByPhone(phone);
    }

    public String findIdByPhone(String phone) {
        return barberRepository.findIdByPhone(phone);
    }
}
