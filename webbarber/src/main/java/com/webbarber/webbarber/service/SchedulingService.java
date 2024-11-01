package com.webbarber.webbarber.service;

import com.webbarber.webbarber.entity.Scheduling;
import com.webbarber.webbarber.repository.SchedulingRepository;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    private final SchedulingRepository schedulingRepository;

    public SchedulingService(SchedulingRepository schedulingRepository) {
        this.schedulingRepository = schedulingRepository;
    }

    public Scheduling bookAppointment(Scheduling scheduling) {
       return this.schedulingRepository.save(scheduling);
    }

    public void cancelAppointment(Scheduling scheduling) {
        this.schedulingRepository.delete(scheduling);
    }
}
