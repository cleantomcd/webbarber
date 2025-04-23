package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlotOverride;
import com.webbarber.webbarber.exception.InvalidDateException;
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

/**
 * Serviço responsável por gerenciar a disponibilidade de horários para agendamentos de barbeiros.
 * Verifica a disponibilidade de horários, manipula horários bloqueados e calcula sequências de horários disponíveis.
 */
@Service
public class TimeSlotAvailabilityService {
    private final BookingRepository bookingRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotOverrideRepository timeSlotOverrideRepository;
    private final ServiceService serviceService;

    /**
     * Construtor para o serviço de disponibilidade de horários.
     *
     * @param bookingRepository         Repositório de agendamentos
     * @param timeSlotRepository        Repositório de horários padrão
     * @param timeSlotOverrideRepository Repositório de horários sobrecarregados (overrides)
     * @param serviceService            Serviço responsável pelos serviços e suas durações
     */
    public TimeSlotAvailabilityService(BookingRepository bookingRepository, TimeSlotRepository timeSlotRepository, TimeSlotOverrideRepository timeSlotOverrideRepository, ServiceService serviceService) {
        this.bookingRepository = bookingRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
        this.serviceService = serviceService;
    }

    /**
     * Verifica se um horário específico está disponível para agendamento.
     *
     * @param barberId   ID do barbeiro
     * @param date       Data do agendamento
     * @param startTime  Hora de início do agendamento
     * @return `true` se o horário estiver disponível, `false` caso contrário
     */
    public boolean isBookingAvailable(String barberId, LocalDate date, LocalTime startTime) {
        if(!getAvailableTimeSlots(barberId, date).contains(startTime)) return false;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date);
        if(bookingRepository.findByBarberIdAndDateAndStartTime(null, date, startTime).isPresent()) return false;
        if(optionalTimeSlotOverride.isPresent() &&
                optionalTimeSlotOverride.get().getClosedSlots().contains(startTime.toString())) {
            return true;
        }
        return bookingRepository.findConflictingBooking(null, date, startTime).isEmpty();
    }

    /**
     * Verifica se um horário está disponível para um serviço específico.
     *
     * @param barberId   ID do barbeiro
     * @param date       Data do agendamento
     * @param startTime  Hora de início do agendamento
     * @param serviceId  ID do serviço
     * @return `true` se o horário e o serviço estiverem disponíveis, `false` caso contrário
     */
    public boolean isBkAvailable(String barberId, LocalDate date, LocalTime startTime, String serviceId) {
        List<LocalTime> availableTimeSlots = getAvailableTimeSlotsByService(barberId, date, serviceId);
        return isBookingAvailable(barberId, date, startTime) && availableTimeSlots.contains(startTime);
    }

    /**
     * Obtém os horários de início de todos os agendamentos para uma data específica.
     *
     * @param date Data para verificar os horários de agendamento
     * @return Lista de horários de início de agendamentos para a data
     */
    public List<LocalTime> findStartTimesByDate(LocalDate date) {
        return bookingRepository.findStartTimesByBarberIdAndDate(null, date);
    }

    /**
     * Obtém a duração de um serviço pelo seu ID.
     *
     * @param serviceId ID do serviço
     * @return Duração do serviço em minutos
     */
    public int getServiceDurationById(String serviceId) {
        return serviceService.getDurationById(null, serviceId);
    }

    /**
     * Converte uma lista de strings representando horários para uma lista de objetos LocalTime.
     *
     * @param slots Lista de strings representando os horários
     * @return Lista de objetos LocalTime
     */
    private List<LocalTime> toLocalTimeList(List<String> slots) {
        return slots == null ? null : slots.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    /**
     * Obtém os horários disponíveis para um horário específico, levando em conta se estão bloqueados ou não.
     *
     * @param timeSlotDTO Objeto DTO com os horários configurados
     * @param closedSlots Lista de slots fechados ou bloqueados
     * @return Lista de horários disponíveis
     */
    private List<LocalTime> getTimeSlots(StandardTimeSlotDTO timeSlotDTO, List<String> closedSlots) {
        List<LocalTime> allTimeSlots;
        allTimeSlots = getAmTimeSlots(timeSlotDTO);
        allTimeSlots.addAll(getPmTimeSlots(timeSlotDTO));
        if(closedSlots == null) return allTimeSlots;
        allTimeSlots.removeAll(toLocalTimeList(closedSlots));
        return allTimeSlots;
    }

    /**
     * Obtém os horários disponíveis no período da tarde, levando em conta o intervalo configurado.
     *
     * @param timeSlotDTO Objeto DTO com as configurações de horário
     * @return Lista de horários da tarde
     */
    private List<LocalTime> getPmTimeSlots(StandardTimeSlotDTO timeSlotDTO) {
        List<LocalTime> pmTimeSlots = new ArrayList<>();
        LocalTime pmStart = timeSlotDTO.pmStartTime();
        LocalTime pmEnd = timeSlotDTO.pmEndTime();
        int interval = timeSlotDTO.interval();

        while(pmStart.isBefore(pmEnd)) {
            pmTimeSlots.add(pmStart);
            pmStart = pmStart.plusMinutes(interval);
        }

        return pmTimeSlots;
    }

    /**
     * Obtém os horários disponíveis no período da manhã, levando em conta o intervalo configurado.
     *
     * @param timeSlotDTO Objeto DTO com as configurações de horário
     * @return Lista de horários da manhã
     */
    private List<LocalTime> getAmTimeSlots(StandardTimeSlotDTO timeSlotDTO) {
        List<LocalTime> amTimeSlots = new ArrayList<>();
        LocalTime amStart = timeSlotDTO.amStartTime();
        LocalTime amEnd = timeSlotDTO.amEndTime();
        int interval = timeSlotDTO.interval();

        while(amStart.isBefore(amEnd)) {
            amTimeSlots.add(amStart);
            amStart = amStart.plusMinutes(interval);
        }

        return amTimeSlots;
    }

    /**
     * Obtém todos os horários disponíveis para agendamento de um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date     Data para verificar os horários disponíveis
     * @return Lista de horários disponíveis
     */
    public List<LocalTime> getAvailableTimeSlots(String barberId, LocalDate date) {
        validateDate(date);

        StandardTimeSlotDTO timeSlot;
        List<LocalTime> allTimeSlots;
        if(timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date).isPresent()) {
            EditedTimeSlotDTO editedTimeSlotDTO =
                    toEditedTimeSlotDTO(timeSlotOverrideRepository.findDTOByBarberIdAndDate(barberId, date));
            if(editedTimeSlotDTO.isClosed()) return null;
            timeSlot = toStandardTimeSlotDTO(editedTimeSlotDTO);
            allTimeSlots = getTimeSlots(timeSlot, editedTimeSlotDTO.closedSlots());
        }
        else {
            timeSlot = timeSlotRepository.findByBarberIdAndDayOfWeek(barberId, date.getDayOfWeek().getValue());
            if(timeSlot == null) return null;
            allTimeSlots = getTimeSlots(timeSlot, null);
        }
        allTimeSlots.removeAll(findStartTimesByDate(date));

        return allTimeSlots;
    }

    /**
     * Converte um DTO de horário editado para um DTO padrão de horário.
     *
     * @param timeSlotDTO DTO de horário editado
     * @return DTO padrão de horário
     */
    private StandardTimeSlotDTO toStandardTimeSlotDTO(EditedTimeSlotDTO timeSlotDTO) {
        return new StandardTimeSlotDTO(timeSlotDTO.date().getDayOfWeek().getValue(), timeSlotDTO.amStartTime(),
                timeSlotDTO.amEndTime(), timeSlotDTO.pmStartTime(), timeSlotDTO.pmEndTime(), timeSlotDTO.interval());
    }

    /**
     * Converte um objeto TimeSlotOverride para um DTO de horário editado.
     *
     * @param timeSlotOverride Objeto TimeSlotOverride
     * @return DTO de horário editado
     */
    private EditedTimeSlotDTO toEditedTimeSlotDTO(TimeSlotOverride timeSlotOverride) {
        return new EditedTimeSlotDTO(timeSlotOverride.getDate(), timeSlotOverride.getAmStartTime(),
                timeSlotOverride.getAmEndTime(), timeSlotOverride.getPmStartTime(), timeSlotOverride.getPmEndTime(),
                timeSlotOverride.getInterval(), timeSlotOverride.getClosedSlots(), timeSlotOverride.isClosed());
    }

    /**
     * Obtém os horários disponíveis para um serviço específico em uma data específica.
     *
     * @param barberId   ID do barbeiro
     * @param date       Data do agendamento
     * @param serviceId  ID do serviço
     * @return Lista de horários disponíveis para o serviço
     */
    public List<LocalTime> getAvailableTimeSlotsByService(String barberId, LocalDate date, String serviceId) {
        validateDate(date);
        List<LocalTime> closedSlots = null;
        List<LocalTime> amTimeSlots;
        List<LocalTime> pmTimeSlots;
        List<LocalTime> availableSlots;
        StandardTimeSlotDTO timeSlot;

        Optional<TimeSlotOverride> optionalTimeSlotOverride =
                timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date);

        if(optionalTimeSlotOverride.isPresent()) {
            EditedTimeSlotDTO editedTimeSlotDTO =
                    toEditedTimeSlotDTO(timeSlotOverrideRepository.findDTOByBarberIdAndDate(barberId, date));
            if(editedTimeSlotDTO.isClosed()) return null;
            timeSlot = toStandardTimeSlotDTO(editedTimeSlotDTO);
            closedSlots = toLocalTimeList(optionalTimeSlotOverride.get().getClosedSlots());
        }
        else {
            timeSlot = timeSlotRepository.findByBarberIdAndDayOfWeek(barberId, date.getDayOfWeek().getValue());
            if(timeSlot == null) return null;
        }

        amTimeSlots = getAmTimeSlots(timeSlot);
        pmTimeSlots = getPmTimeSlots(timeSlot);
        availableSlots = getAvailableSequence(barberId, amTimeSlots, date, serviceId);
        availableSlots.addAll(getAvailableSequence(barberId, pmTimeSlots, date, serviceId));
        if(closedSlots != null) availableSlots.removeAll(closedSlots);
        return availableSlots;
    }

    /**
     * Verifica se existe uma sequência de horários disponível para um serviço específico.
     *
     * @param barberId   ID do barbeiro
     * @param slots      Lista de slots de horário
     * @param date       Data do agendamento
     * @param serviceId  ID do serviço
     * @return Lista de horários disponíveis para o serviço
     */
    private List<LocalTime> getAvailableSequence(String barberId, List<LocalTime> slots, LocalDate date, String serviceId) {
        List<LocalTime> availableSequence = new ArrayList<>();
        int duration;
        if(serviceId == null) duration = 1;
        else duration = getServiceDurationById(serviceId);
        boolean isSequenceAvailable;
        LocalTime slot;
        LocalTime nextSlot;
        int interval = getInterval(barberId, date);

        for (int i = 0; i < slots.size() - (duration - 1); i++) {
            isSequenceAvailable = true;
            slot = slots.get(i);
            for (int j = 1; j < duration; j++) {
                nextSlot = slot.plusMinutes(interval * j);
                if(!slots.contains(nextSlot)) {
                    isSequenceAvailable = false;
                    break;
                }
            }
            if(isSequenceAvailable) availableSequence.add(slot);
        }
        return availableSequence;
    }

    /**
     * Calcula o intervalo entre os horários de trabalho.
     *
     * @param barberId ID do barbeiro
     * @param date     Data para verificar o intervalo
     * @return Intervalo em minutos entre os horários de trabalho
     */
    public int getInterval(String barberId, LocalDate date) {
        StandardTimeSlotDTO timeSlotDTO = timeSlotRepository.findByBarberIdAndDayOfWeek(barberId, date.getDayOfWeek().getValue());
        return timeSlotDTO.interval();
    }

    /**
     * Valida a data para garantir que seja uma data futura.
     *
     * @param date Data a ser validada
     * @throws InvalidDateException Se a data não for válida
     */
    private void validateDate(LocalDate date) {
        if(date.isBefore(LocalDate.now())) throw new InvalidDateException("invalid date");
    }
}
