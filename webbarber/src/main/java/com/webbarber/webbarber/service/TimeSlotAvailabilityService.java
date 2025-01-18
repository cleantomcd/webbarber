package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlotOverride;
import com.webbarber.webbarber.exception.InvalidDateException;
import com.webbarber.webbarber.repository.BookingRepository;
import com.webbarber.webbarber.repository.TimeSlotOverrideRepository;
import com.webbarber.webbarber.repository.TimeSlotRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotAvailabilityService {
    private final BookingRepository bookingRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotOverrideRepository timeSlotOverrideRepository;
    private final ServiceService serviceService;

    public TimeSlotAvailabilityService(BookingRepository bookingRepository, TimeSlotRepository timeSlotRepository, TimeSlotOverrideRepository timeSlotOverrideRepository, ServiceService serviceService) {
        this.bookingRepository = bookingRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
        this.serviceService = serviceService;
    }

    public boolean isBookingAvailable(LocalDate date, LocalTime startTime) {
        if(!getAvailableTimeSlots(date).contains(startTime)) return false;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(bookingRepository.findByDateAndStartTime(date, startTime).isPresent()) return false;
        if(optionalTimeSlotOverride.isPresent() &&
                optionalTimeSlotOverride.get().getClosedSlots().contains(startTime.toString())) {
            return true;
        }
        return bookingRepository.findConflictingBooking(date, startTime).isEmpty();
    }

    public boolean isBkAvailable(LocalDate date, LocalTime startTime, String serviceId) {
        List<LocalTime> availableTimeSlots = getAvailableTimeSlotsByService(date, serviceId);
        return isBookingAvailable(date, startTime) && availableTimeSlots.contains(startTime);
    }

    public List<LocalTime> findStartTimesByDate(LocalDate date) {
        return bookingRepository.findStartTimesByDate(date);
    }

    public int getServiceDurationById(String serviceId) {
        return serviceService.getDurationById(serviceId);
    }

    private List<LocalTime> toLocalTimeList(List<String> slots) {
        return slots == null ? null : slots.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    private List<LocalTime> getTimeSlots(StandardTimeSlotDTO timeSlotDTO, List<String> closedSlots) {
        List<LocalTime> allTimeSlots;
        allTimeSlots = getAmTimeSlots(timeSlotDTO);
        allTimeSlots.addAll(getPmTimeSlots(timeSlotDTO));
        if(closedSlots == null) return allTimeSlots;
        allTimeSlots.removeAll(toLocalTimeList(closedSlots));
        return allTimeSlots;
    }

    private List<LocalTime> getPmTimeSlots(StandardTimeSlotDTO timeSlotDTO) {
        List<LocalTime> pmTimeSlots = new ArrayList<LocalTime>();
        LocalTime pmStart = timeSlotDTO.pmStartTime();
        LocalTime pmEnd = timeSlotDTO.pmEndTime();
        int interval = timeSlotDTO.interval();

        while(pmStart.isBefore(pmEnd)) {
            pmTimeSlots.add(pmStart);
            pmStart = pmStart.plusMinutes(interval);
        }

        return pmTimeSlots;
    }

    private List<LocalTime> getAmTimeSlots(StandardTimeSlotDTO timeSlotDTO) {
        List<LocalTime> amTimeSlots = new ArrayList<LocalTime>();
        LocalTime amStart = timeSlotDTO.amStartTime();
        LocalTime amEnd = timeSlotDTO.amEndTime();
        int interval = timeSlotDTO.interval();
        while(amStart.isBefore(amEnd)) {
            amTimeSlots.add(amStart);
            amStart = amStart.plusMinutes(interval);
        }

        return amTimeSlots;
    }

    public List<LocalTime> getAvailableTimeSlots(LocalDate date) {
        validateDate(date);

        StandardTimeSlotDTO timeSlot;
        List<LocalTime> allTimeSlots;
        if(timeSlotOverrideRepository.findByDate(date).isPresent()) {
            EditedTimeSlotDTO editedTimeSlotDTO = toEditedTimeSlotDTO(timeSlotOverrideRepository.findDTOByDate(date));
            if(editedTimeSlotDTO.isClosed()) return null;
            timeSlot = toStandardTimeSlotDTO(editedTimeSlotDTO);
            allTimeSlots = getTimeSlots(timeSlot, editedTimeSlotDTO.closedSlots());
        }
        else {
            timeSlot = timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue());
            if(timeSlot == null) return null;
            allTimeSlots = getTimeSlots(timeSlot, null);
        }
        allTimeSlots.removeAll(findStartTimesByDate(date));

        return allTimeSlots;
    }

    private StandardTimeSlotDTO toStandardTimeSlotDTO(EditedTimeSlotDTO timeSlotDTO) {
        return new StandardTimeSlotDTO(timeSlotDTO.date().getDayOfWeek().getValue(), timeSlotDTO.amStartTime(),
                timeSlotDTO.amEndTime(), timeSlotDTO.pmStartTime(), timeSlotDTO.pmEndTime(), timeSlotDTO.interval());
    }

    private EditedTimeSlotDTO toEditedTimeSlotDTO(TimeSlotOverride timeSlotOverride) {
        return new EditedTimeSlotDTO(timeSlotOverride.getDate(), timeSlotOverride.getAmStartTime(),
                timeSlotOverride.getAmEndTime(), timeSlotOverride.getPmStartTime(), timeSlotOverride.getPmEndTime(),
                timeSlotOverride.getInterval(), timeSlotOverride.getClosedSlots(), timeSlotOverride.isClosed());
    }

    public List<LocalTime> getAvailableTimeSlotsByService(LocalDate date, String serviceId) {
        validateDate(date);
        List<LocalTime> closedSlots = null;
        List<LocalTime> amTimeSlots;
        List<LocalTime> pmTimeSlots;
        List<LocalTime> availableSlots;
        StandardTimeSlotDTO timeSlot;

        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        if(optionalTimeSlotOverride.isPresent()) {
            EditedTimeSlotDTO editedTimeSlotDTO = toEditedTimeSlotDTO(timeSlotOverrideRepository.findDTOByDate(date));
            if(editedTimeSlotDTO.isClosed()) return null;
            timeSlot = toStandardTimeSlotDTO(editedTimeSlotDTO);
            closedSlots = toLocalTimeList(optionalTimeSlotOverride.get().getClosedSlots());
        }
        else {
            timeSlot = timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue());
            if(timeSlot == null) return null;

        }
        amTimeSlots = getAmTimeSlots(timeSlot);
        pmTimeSlots = getPmTimeSlots(timeSlot);
        availableSlots = getAvailableSequence(amTimeSlots, date, serviceId);
        availableSlots.addAll(getAvailableSequence(pmTimeSlots, date, serviceId));
        if(closedSlots != null) availableSlots.removeAll(closedSlots);
        return availableSlots;
    }

    private List<LocalTime> getAvailableSequence(List<LocalTime> slots, LocalDate date, String serviceId) {
        List<LocalTime> availableSequence = new ArrayList<>();
        int duration;
        if(serviceId == null) duration = 1;
        else duration = getServiceDurationById(serviceId);
        boolean isSequenceAvailable;
        LocalTime slot;
        LocalTime nextSlot;
        int interval = getInterval(date);

        for (int i = 0; i < slots.size() - (duration - 1); i++) {
            isSequenceAvailable = true;
            slot = slots.get(i);
            nextSlot = slot;
            for (int j = 0; j < duration; j++) {
                if (!isBookingAvailable(date, nextSlot)) {
                    isSequenceAvailable = false;
                    break;
                }
                nextSlot = nextSlot.plusMinutes(interval);
            }
            if (isSequenceAvailable) availableSequence.add(slot);
        }

        return availableSequence;
    }

    public int getInterval(LocalDate date) {
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByDate(date);
        return optionalTimeSlotOverride.map(TimeSlotOverride::getInterval).orElseGet(() ->
                timeSlotRepository.findByDayOfWeek(date.getDayOfWeek().getValue()).interval());
    }

    private void validateDate(LocalDate date) {
        if(date.isBefore(LocalDate.now())) throw new InvalidDateException("Data inválida");
    }
}
