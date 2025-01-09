package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlot;
import com.webbarber.webbarber.entity.TimeSlotOverride;
import com.webbarber.webbarber.repository.BookingRepository;
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
    private final BookingRepository bookingRepository;
    private final TimeSlotOverrideRepository timeSlotOverrideRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository,
                           BookingRepository bookingRepository,
                           TimeSlotOverrideRepository timeSlotOverrideRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.bookingRepository = bookingRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
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

    public void removeTimeSlotOverride(LocalDate date) {
        TimeSlotOverride timeSlotOverride = timeSlotOverrideRepository.findDTOByDate(date);
        timeSlotOverrideRepository.delete(timeSlotOverride);
    }

    public void editTimeSlot(EditedTimeSlotDTO editedTimeSlotDTO) {
        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.
                findByDate(editedTimeSlotDTO.date());
        timeSlotOverride = optionalTimeSlotOverride.orElseGet(() -> new TimeSlotOverride(editedTimeSlotDTO));
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

    private List<LocalTime> toLocalTimeList(List<String> slots) {
        return slots == null ? null : slots.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    private List<LocalTime> getTimeSlots(StandardTimeSlotDTO timeSlotDTO, List<String> closedSlots) {
        List<LocalTime> allTimeSlots = new ArrayList<LocalTime>();
        LocalTime amStart = timeSlotDTO.amStartTime();
        LocalTime pmStart = timeSlotDTO.pmStartTime();
        LocalTime amEnd = timeSlotDTO.amEndTime();
        LocalTime pmEnd = timeSlotDTO.pmEndTime();
        int interval = timeSlotDTO.interval();
        while(amStart.isBefore(amEnd)) {
            allTimeSlots.add(amStart);
            amStart = amStart.plusMinutes(interval);
        }
        while(pmStart.isBefore(pmEnd)) {
            allTimeSlots.add(pmStart);
            pmStart = pmStart.plusMinutes(interval);
        }

        if(closedSlots != null) allTimeSlots.removeAll(toLocalTimeList(closedSlots));
        return allTimeSlots;
    }

    public List<LocalTime> getAvailableTimeSlots(LocalDate date) {
        StandardTimeSlotDTO timeSlot;
        List<LocalTime> allTimeSlots;
        if(timeSlotOverrideRepository.findByDate(date).isPresent()) {
            EditedTimeSlotDTO editedTimeSlotDTO = toEditedTimeSlotDTO(timeSlotOverrideRepository.findDTOByDate(date));
            if(editedTimeSlotDTO.isClosed()) return null;
            System.out.println(editedTimeSlotDTO.toString());

            timeSlot = new StandardTimeSlotDTO(editedTimeSlotDTO.date().getDayOfWeek().getValue(), editedTimeSlotDTO.amStartTime(),
                    editedTimeSlotDTO.amEndTime(), editedTimeSlotDTO.pmStartTime(), editedTimeSlotDTO.pmEndTime(),
                    editedTimeSlotDTO.interval());
             allTimeSlots = getTimeSlots(timeSlot, editedTimeSlotDTO.closedSlots());
        }
        else {
            timeSlot = timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue());
            if(timeSlot == null) return null;
            allTimeSlots = getTimeSlots(timeSlot, null);
        }
        allTimeSlots.removeAll(bookingRepository.findStartTimesByDate(date));
        return allTimeSlots;
    }

    public List<LocalDate> getDatesWithSpecificTime(LocalTime time) {
        List<LocalDate> dates = new ArrayList<LocalDate>();
        LocalDate date = LocalDate.now();
        List<LocalTime> slots;
        LocalDate datePlus14 = date.plusDays(14);
        while(date.isBefore(datePlus14)) {
            slots = getAvailableTimeSlots(date);
            if(slots != null && slots.contains(time)) dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }

    private EditedTimeSlotDTO toEditedTimeSlotDTO(TimeSlotOverride timeSlotOverride) {
        return new EditedTimeSlotDTO(timeSlotOverride.getDate(), timeSlotOverride.getAmStartTime(),
                timeSlotOverride.getAmEndTime(), timeSlotOverride.getPmStartTime(), timeSlotOverride.getPmEndTime(),
                timeSlotOverride.getInterval(), timeSlotOverride.getClosedSlots(), timeSlotOverride.isClosed());
    }

    // lógica para verificar horário disponível no caso de serviços duplos: 'se horário + interval está disponível,
    // então adicionar horário + interval' -----> obter horários disponíveis com base no serviço.
}
