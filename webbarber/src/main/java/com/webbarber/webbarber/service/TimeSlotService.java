package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlot;
import com.webbarber.webbarber.entity.TimeSlotOverride;
import com.webbarber.webbarber.repository.TimeSlotOverrideRepository;
import com.webbarber.webbarber.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotOverrideRepository timeSlotOverrideRepository;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;

    public TimeSlotService(TimeSlotRepository timeSlotRepository,
                           TimeSlotOverrideRepository timeSlotOverrideRepository,
                           TimeSlotAvailabilityService timeSlotAvailabilityService) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
    }

    public void setTimeSlot(StandardTimeSlotDTO standardTimeSlotDTO) {
        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.
                optionalFindByDayOfWeek(standardTimeSlotDTO.dayOfWeek());
        TimeSlot newTimeSlot;
        if(optionalTimeSlot.isPresent()) {
            newTimeSlot = optionalTimeSlot.get();
            updateTimeSlotFromDTO(newTimeSlot, standardTimeSlotDTO);
        }
        else newTimeSlot = new TimeSlot(standardTimeSlotDTO);

        timeSlotRepository.save(newTimeSlot);
    }

    private void updateTimeSlotFromDTO(TimeSlot timeSlot, StandardTimeSlotDTO dto) {
        timeSlot.setAmStartTime(dto.amStartTime());
        timeSlot.setAmEndTime(dto.amEndTime());
        timeSlot.setPmStartTime(dto.pmStartTime());
        timeSlot.setPmEndTime(dto.pmEndTime());
        timeSlot.setInterval(dto.interval());
    }

    private void updateTimeSlotOverrideFromDTO(TimeSlotOverride timeSlotOverride, EditedTimeSlotDTO dto) {
        timeSlotOverride.setAmStartTime(dto.amStartTime());
        timeSlotOverride.setAmEndTime(dto.amEndTime());
        timeSlotOverride.setPmStartTime(dto.pmStartTime());
        timeSlotOverride.setPmEndTime(dto.pmEndTime());
        timeSlotOverride.setInterval(dto.interval());
        timeSlotOverride.setClosed(dto.isClosed());
        timeSlotOverride.setClosedSlots(dto.closedSlots());
    }

    public void removeTimeSlotOverride(LocalDate date) {
        TimeSlotOverride timeSlotOverride = timeSlotOverrideRepository.findDTOByDate(date);
        timeSlotOverrideRepository.delete(timeSlotOverride);
    }

    public void editTimeSlot(EditedTimeSlotDTO editedTimeSlotDTO) {
        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.
                findByDate(editedTimeSlotDTO.date());
        if(optionalTimeSlotOverride.isPresent()) {
            timeSlotOverride = optionalTimeSlotOverride.get();
            updateTimeSlotOverrideFromDTO(timeSlotOverride, editedTimeSlotDTO);
        }
        else timeSlotOverride = new TimeSlotOverride(editedTimeSlotDTO);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    public void closeDate(LocalDate date) {
        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isPresent()) {
            timeSlotOverride = optionalTimeSlotOverride.get();
            timeSlotOverride.setClosed(true);
        }
        else timeSlotOverride = new TimeSlotOverride(date);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    public void addClosedSlots(LocalDate date, List<String> slots) {
        StandardTimeSlotDTO timeSlotDTO = timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue());
        EditedTimeSlotDTO editedTimeSlotDTO = new EditedTimeSlotDTO(date, timeSlotDTO.amStartTime(),
                timeSlotDTO.amEndTime(), timeSlotDTO.pmStartTime(), timeSlotDTO.pmEndTime(), timeSlotDTO.interval(),
                slots, false);
        TimeSlotOverride timeSlotOverride = new TimeSlotOverride(editedTimeSlotDTO);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    public void removeClosedSlots(LocalDate date, List<String> slots) {
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isPresent()) {
            TimeSlotOverride timeSlotOverride = optionalTimeSlotOverride.get();
            timeSlotOverride.removeClosedSlots(slots);
        }
    }

    public void clearClosedSlots(LocalDate date) {
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isPresent()) {
            TimeSlotOverride timeSlotOverride = optionalTimeSlotOverride.get();
            timeSlotOverride.clearClosedSlots();
        }
    }

}
