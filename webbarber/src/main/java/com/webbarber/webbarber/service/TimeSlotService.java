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

/**
 * Serviço que gerencia a criação, edição e remoção de slots de tempo para barbeiros.
 */
@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotOverrideRepository timeSlotOverrideRepository;

    /**
     * Construtor do serviço de slots de tempo.
     *
     * @param timeSlotRepository Repositório para persistência de slots de tempo
     * @param timeSlotOverrideRepository Repositório para persistência de sobrecarga de slots de tempo
     */
    public TimeSlotService(TimeSlotRepository timeSlotRepository,
                           TimeSlotOverrideRepository timeSlotOverrideRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotOverrideRepository = timeSlotOverrideRepository;
    }

    /**
     * Define os slots de tempo padrão para um barbeiro.
     *
     * @param barberId ID do barbeiro
     * @param standardTimeSlotDTO DTO contendo os detalhes dos slots de tempo
     */
    public void setTimeSlot(String barberId, StandardTimeSlotDTO standardTimeSlotDTO) {
        validateTimeSlot(standardTimeSlotDTO);

        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.
                optionalFindByBarberIdAndDayOfWeek(barberId, standardTimeSlotDTO.dayOfWeek());
        TimeSlot newTimeSlot;
        if(optionalTimeSlot.isPresent()) {
            newTimeSlot = optionalTimeSlot.get();
            updateTimeSlotFromDTO(newTimeSlot, standardTimeSlotDTO);
        }
        else newTimeSlot = new TimeSlot(barberId, standardTimeSlotDTO);

        timeSlotRepository.save(newTimeSlot);
    }

    /**
     * Valida os dados fornecidos para o slot de tempo.
     *
     * @param standardTimeSlotDTO DTO com os dados do slot de tempo
     * @throws InvalidDateException Se a data fornecida for inválida
     * @throws InvalidDayOfWeekException Se o dia da semana fornecido for inválido
     * @throws InvalidStartTimeException Se o horário de início for depois do horário de fim
     * @throws InvalidTimeIntervalException Se o intervalo entre os horários for inválido
     */
    private void validateTimeSlot(StandardTimeSlotDTO standardTimeSlotDTO) {
        validateDayOfWeek(standardTimeSlotDTO.dayOfWeek());
        validateTimeStarts(standardTimeSlotDTO.amStartTime(), standardTimeSlotDTO.amEndTime());
        validateTimeStarts(standardTimeSlotDTO.pmStartTime(), standardTimeSlotDTO.pmEndTime());
        validateInterval(standardTimeSlotDTO.interval());
    }

    /**
     * Valida os dados para a edição de um slot de tempo.
     *
     * @param editedTimeSlotDTO DTO com os dados do slot de tempo editado
     * @throws InvalidDateException Se a data fornecida for inválida
     * @throws InvalidStartTimeException Se o horário de início for depois do horário de fim
     * @throws InvalidTimeIntervalException Se o intervalo entre os horários for inválido
     */
    private void validateEditedTimeSlot(EditedTimeSlotDTO editedTimeSlotDTO) {
        validateDate(editedTimeSlotDTO.date());
        validateTimeStarts(editedTimeSlotDTO.amStartTime(), editedTimeSlotDTO.amEndTime());
        validateTimeStarts(editedTimeSlotDTO.pmStartTime(), editedTimeSlotDTO.pmEndTime());
        validateInterval(editedTimeSlotDTO.interval());
    }

    /**
     * Valida se a data fornecida é válida (não pode ser uma data no passado).
     *
     * @param date Data a ser validada
     * @throws InvalidDateException Se a data for inválida (anterior a data atual)
     */
    private void validateDate(LocalDate date) {
        if(date.isBefore(LocalDate.now())) throw new InvalidDateException("Selecione uma data válida.");
    }

    /**
     * Valida o dia da semana fornecido.
     *
     * @param dayOfWeek Número representando o dia da semana (1-7)
     * @throws InvalidDayOfWeekException Se o número do dia da semana for inválido
     */
    private void validateDayOfWeek(int dayOfWeek) {
        if(dayOfWeek > 7 || dayOfWeek < 1) throw new InvalidDayOfWeekException("Selecione um dia da semana válido");
    }

    /**
     * Valida se o horário de início é anterior ao horário de fim.
     *
     * @param startTime Hora de início
     * @param endTime Hora de fim
     * @throws InvalidStartTimeException Se o horário de início for posterior ao horário de fim
     */
    private void validateTimeStarts(LocalTime startTime, LocalTime endTime) {
        if(startTime.isAfter(endTime)) throw new InvalidStartTimeException("O horário de início não pode ser depois" +
                "do horário de fim");
    }

    /**
     * Valida se o intervalo entre os horários está dentro do permitido (20 a 60 minutos).
     *
     * @param interval Intervalo entre os horários em minutos
     * @throws InvalidTimeIntervalException Se o intervalo for inválido
     */
    private void validateInterval(int interval) {
        if(interval < 20 || interval > 60) throw new InvalidTimeIntervalException("O intervalo entre horários deve ser" +
                "de 20 a 60 minutos.");
    }

    /**
     * Atualiza os dados de um slot de tempo com as informações do DTO.
     *
     * @param timeSlot Slot de tempo a ser atualizado
     * @param dto DTO com os novos dados
     */
    private void updateTimeSlotFromDTO(TimeSlot timeSlot, StandardTimeSlotDTO dto) {
        timeSlot.setAmStartTime(dto.amStartTime());
        timeSlot.setAmEndTime(dto.amEndTime());
        timeSlot.setPmStartTime(dto.pmStartTime());
        timeSlot.setPmEndTime(dto.pmEndTime());
        timeSlot.setInterval(dto.interval());
    }

    /**
     * Atualiza os dados de uma sobrecarga de slot de tempo com as informações do DTO.
     *
     * @param timeSlotOverride Sobrecarga de slot de tempo a ser atualizada
     * @param dto DTO com os novos dados
     */
    private void updateTimeSlotOverrideFromDTO(TimeSlotOverride timeSlotOverride, EditedTimeSlotDTO dto) {
        timeSlotOverride.setAmStartTime(dto.amStartTime());
        timeSlotOverride.setAmEndTime(dto.amEndTime());
        timeSlotOverride.setPmStartTime(dto.pmStartTime());
        timeSlotOverride.setPmEndTime(dto.pmEndTime());
        timeSlotOverride.setInterval(dto.interval());
        timeSlotOverride.setClosed(dto.isClosed());
        timeSlotOverride.setClosedSlots(dto.closedSlots());
    }

    /**
     * Remove uma sobrecarga de slot de tempo de um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date Data do slot de tempo a ser removido
     */
    public void removeTimeSlotOverride(String barberId, LocalDate date) {
        TimeSlotOverride timeSlotOverride = timeSlotOverrideRepository.findDTOByBarberIdAndDate(barberId, date);
        timeSlotOverrideRepository.delete(timeSlotOverride);
    }

    /**
     * Edita a sobrecarga de slots de tempo de um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param editedTimeSlotDTO DTO com os novos dados para edição
     */
    public void editTimeSlot(String barberId, EditedTimeSlotDTO editedTimeSlotDTO) {
        validateEditedTimeSlot(editedTimeSlotDTO);

        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.
                findByBarberIdAndDate(barberId, editedTimeSlotDTO.date());
        if(optionalTimeSlotOverride.isPresent()) {
            timeSlotOverride = optionalTimeSlotOverride.get();
            updateTimeSlotOverrideFromDTO(timeSlotOverride, editedTimeSlotDTO);
        }
        else timeSlotOverride = new TimeSlotOverride(barberId, editedTimeSlotDTO);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    /**
     * Define a disponibilidade (aberto/fechado) de um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date Data para definir a disponibilidade
     * @param isOpen Estado de abertura (true para aberto, false para fechado)
     */
    public void setDataAvailability(String barberId, LocalDate date, boolean isOpen) {
        validateDate(date);

        TimeSlotOverride timeSlotOverride;
        Optional<TimeSlotOverride> optionalTimeSlotOverride = timeSlotOverrideRepository.
                findByBarberIdAndDate(barberId, date);

        if(optionalTimeSlotOverride.isPresent()) {
            timeSlotOverride = optionalTimeSlotOverride.get();
            timeSlotOverride.setClosed(!isOpen);
            return;
        }

        timeSlotOverride = new TimeSlotOverride(date, timeSlotRepository.
                findByBarberIdAndDayOfWeek(barberId, date.getDayOfWeek().getValue()), isOpen);
        timeSlotOverrideRepository.save(timeSlotOverride);
    }

    /**
     * Adiciona slots fechados a uma sobrecarga de slot de tempo para um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date Data do slot de tempo
     * @param slots Lista de slots a serem fechados
     */
    public void addClosedSlots(String barberId, LocalDate date, List<String> slots) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride =
                timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date);

        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().addClosedSlots(slots);
    }

    /**
     * Remove slots fechados de uma sobrecarga de slot de tempo para um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date Data do slot de tempo
     * @param slots Lista de slots a serem removidos
     */
    public void removeClosedSlots(String barberId, LocalDate date, List<String> slots) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride =
                timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date);

        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().removeClosedSlots(slots);
    }

    /**
     * Limpa todos os slots fechados de uma sobrecarga de slot de tempo para um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro
     * @param date Data do slot de tempo
     */
    public void clearClosedSlots(String barberId, LocalDate date) {
        validateDate(date);

        Optional<TimeSlotOverride> optionalTimeSlotOverride =
                timeSlotOverrideRepository.findByBarberIdAndDate(barberId, date);

        if(optionalTimeSlotOverride.isEmpty()) throw new TimeSlotNotFoundException("Data inválida");
        optionalTimeSlotOverride.get().clearClosedSlots();
    }
}
