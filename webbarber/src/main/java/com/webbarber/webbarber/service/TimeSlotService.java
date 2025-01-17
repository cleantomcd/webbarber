package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlot;
import com.webbarber.webbarber.entity.TimeSlotOverride;
import com.webbarber.webbarber.exception.*;
import com.webbarber.webbarber.repository.TimeSlotOverrideRepository;
import com.webbarber.webbarber.repository.TimeSlotRepository;
import org.springframework.cglib.core.Local;
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

    public TimeSlotService(TimeSlotRepository timeSlotRepository,
                           TimeSlotOverrideRepository timeSlotOverrideRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
    }

    public void setTimeSlot(StandardTimeSlotDTO standardTimeSlotDTO) {
        validateTimeSlot(standardTimeSlotDTO);

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

    private void validateTimeSlot(StandardTimeSlotDTO standardTimeSlotDTO) {
        validateDayOfWeek(standardTimeSlotDTO.dayOfWeek());
        validateTimeStarts(standardTimeSlotDTO.amStartTime(), standardTimeSlotDTO.amEndTime());
        validateTimeStarts(standardTimeSlotDTO.pmStartTime(), standardTimeSlotDTO.pmEndTime());
        validateInterval(standardTimeSlotDTO.interval());
    }

    private void validateEditedTimeSlot(EditedTimeSlotDTO editedTimeSlotDTO) {
        validateDate(editedTimeSlotDTO.date());
        validateTimeStarts(editedTimeSlotDTO.amStartTime(), editedTimeSlotDTO.amEndTime());
        validateTimeStarts(editedTimeSlotDTO.pmStartTime(), editedTimeSlotDTO.pmEndTime());
        validateInterval(editedTimeSlotDTO.interval());
    }

    private void validateDate(LocalDate date) {
        if(date.isBefore(LocalDate.now())) throw new InvalidDateException("Selecione uma data válida.");
    }

    private void validateDayOfWeek(int dayOfWeek) {
        if(dayOfWeek > 7 || dayOfWeek < 1) throw new InvalidDayOfWeekException("Selecione um dia da semana válido");
    }

    private void validateTimeStarts(LocalTime startTime, LocalTime endTime) {
        if(startTime.isAfter(endTime)) throw new InvalidStartTimeException("O horário de início não pode ser depois" +
                "do horário de fim");
    }

    private void validateInterval(int interval) {
        if(interval < 20 || interval > 60) throw new InvalidTimeIntervalException("O intervalo entre horários deve ser" +
                "de 20 a 60 minutos.");
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
        validateEditedTimeSlot(editedTimeSlotDTO);

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

    public void setDataAvailability(LocalDate date, boolean isOpen) {
        validateDate(date);

        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isPresent()) {
            timeSlotOverride = optionalTimeSlotOverride.get();
            timeSlotOverride.setClosed(!isOpen);
            return;
        }
        timeSlotOverride = new TimeSlotOverride(date, timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue()), isOpen);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    public void addClosedSlots(LocalDate date, List<String> slots) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().addClosedSlots(slots);
    }

    public void removeClosedSlots(LocalDate date, List<String> slots) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().removeClosedSlots(slots);
    }

    public void clearClosedSlots(LocalDate date) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().clearClosedSlots();
    }

}
