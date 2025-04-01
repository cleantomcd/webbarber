package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.EditedTimeSlotDTO;
import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade que representa um horário específico de atendimento para um barbeiro em uma data determinada.
 * A classe é mapeada para a tabela "timeslot_override" no banco de dados.
 * Ela permite definir horários personalizados para um barbeiro em dias específicos, substituindo os horários padrão definidos para ele.
 * Também é possível bloquear determinados horários de agendamento nesse dia.
 */
@Table(name = "timeslot_override")
@Entity(name = "TimeslotOverride")
public class TimeSlotOverride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String barberId; // ID do barbeiro que terá os horários alterados
    private LocalDate date; // Data específica para a alteração do horário
    private LocalTime amStartTime; // Hora de início do turno da manhã para a data específica
    private LocalTime amEndTime; // Hora de término do turno da manhã para a data específica
    private LocalTime pmStartTime; // Hora de início do turno da tarde para a data específica
    private LocalTime pmEndTime; // Hora de término do turno da tarde para a data específica
    private int interval; // Intervalo entre os horários de agendamento para a data específica

    @ElementCollection
    private List<String> closedSlots; // Lista de horários específicos que estão bloqueados para agendamentos

    private boolean isClosed; // Indica se os horários para a data estão fechados para agendamentos

    /**
     * Construtor padrão necessário para a JPA.
     */
    public TimeSlotOverride() {}

    /**
     * Construtor que cria um TimeSlotOverride a partir de um objeto {@link EditedTimeSlotDTO}.
     *
     * @param barberId ID do barbeiro que terá o horário alterado.
     * @param editedTimeSlotDTO Objeto DTO contendo os dados da alteração do horário para o dia específico.
     */
    public TimeSlotOverride(String barberId, EditedTimeSlotDTO editedTimeSlotDTO) {
        this.barberId = barberId;
        this.date = editedTimeSlotDTO.date();
        this.amStartTime = editedTimeSlotDTO.amStartTime();
        this.amEndTime = editedTimeSlotDTO.amEndTime();
        this.pmStartTime = editedTimeSlotDTO.pmStartTime();
        this.pmEndTime = editedTimeSlotDTO.pmEndTime();
        this.interval = editedTimeSlotDTO.interval();
        this.isClosed = editedTimeSlotDTO.isClosed();
        this.closedSlots = editedTimeSlotDTO.closedSlots();
    }

    /**
     * Construtor que cria um TimeSlotOverride a partir de um objeto {@link StandardTimeSlotDTO}.
     * O horário será marcado como aberto ou fechado conforme o valor de isOpen.
     *
     * @param date Data para a qual os horários específicos serão definidos.
     * @param timeSlot Objeto DTO contendo os dados dos horários para esse dia.
     * @param isOpen Flag que indica se o horário está aberto ou fechado para agendamentos.
     */
    public TimeSlotOverride(LocalDate date, StandardTimeSlotDTO timeSlot, boolean isOpen) {
        this.date = date;
        this.amStartTime = timeSlot.amStartTime();
        this.amEndTime = timeSlot.amEndTime();
        this.pmStartTime = timeSlot.pmStartTime();
        this.pmEndTime = timeSlot.pmEndTime();
        this.interval = timeSlot.interval();
        this.isClosed = !isOpen; // Marca como fechado se isOpen for false
        this.closedSlots = null;
    }

    /**
     * Obtém o ID do horário específico para a data.
     *
     * @return O ID do horário.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o ID do barbeiro que terá o horário alterado para esta data.
     *
     * @return O ID do barbeiro.
     */
    public String getBarberId() {
        return barberId;
    }

    /**
     * Obtém a data em que o horário específico será aplicado.
     *
     * @return A data do horário específico.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Obtém a hora de início do turno da manhã para este dia específico.
     *
     * @return A hora de início do turno da manhã.
     */
    public LocalTime getAmStartTime() {
        return amStartTime;
    }

    /**
     * Obtém a hora de término do turno da manhã para este dia específico.
     *
     * @return A hora de término do turno da manhã.
     */
    public LocalTime getAmEndTime() {
        return amEndTime;
    }

    /**
     * Obtém a hora de início do turno da tarde para este dia específico.
     *
     * @return A hora de início do turno da tarde.
     */
    public LocalTime getPmStartTime() {
        return pmStartTime;
    }

    /**
     * Obtém a hora de término do turno da tarde para este dia específico.
     *
     * @return A hora de término do turno da tarde.
     */
    public LocalTime getPmEndTime() {
        return pmEndTime;
    }

    /**
     * Obtém o intervalo entre os horários de agendamento para este dia específico.
     *
     * @return O intervalo, em minutos.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Verifica se os horários para esta data estão fechados para agendamentos.
     *
     * @return True se os horários estão fechados, false caso contrário.
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Define a data para este horário específico.
     *
     * @param date A nova data.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Define a hora de início do turno da manhã para este horário específico.
     *
     * @param amStartTime A nova hora de início do turno da manhã.
     */
    public void setAmStartTime(LocalTime amStartTime) {
        this.amStartTime = amStartTime;
    }

    /**
     * Define a hora de término do turno da manhã para este horário específico.
     *
     * @param amEndTime A nova hora de término do turno da manhã.
     */
    public void setAmEndTime(LocalTime amEndTime) {
        this.amEndTime = amEndTime;
    }

    /**
     * Define a hora de início do turno da tarde para este horário específico.
     *
     * @param pmStartTime A nova hora de início do turno da tarde.
     */
    public void setPmStartTime(LocalTime pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    /**
     * Define a hora de término do turno da tarde para este horário específico.
     *
     * @param pmEndTime A nova hora de término do turno da tarde.
     */
    public void setPmEndTime(LocalTime pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    /**
     * Define o intervalo entre os horários de agendamento para este horário específico.
     *
     * @param interval O novo intervalo, em minutos.
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Define se os horários para este dia estão fechados para agendamentos.
     *
     * @param closed True se os horários estiverem fechados, false caso contrário.
     */
    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    /**
     * Obtém a lista de horários fechados para este horário específico.
     *
     * @return A lista de horários fechados.
     */
    public List<String> getClosedSlots() {
        return closedSlots;
    }

    /**
     * Define a lista de horários fechados para este horário específico.
     *
     * @param closedSlots A nova lista de horários fechados.
     */
    public void setClosedSlots(List<String> closedSlots) {
        this.closedSlots = closedSlots;
    }

    /**
     * Converte a lista de horários fechados de String para LocalTime.
     *
     * @return A lista de horários fechados convertidos para LocalTime.
     */
    public List<LocalTime> getParsedClosedSlots() {
        return this.closedSlots.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    /**
     * Adiciona novos horários à lista de horários fechados para este dia específico.
     *
     * @param slots Lista de horários a serem adicionados aos fechados.
     */
    public void addClosedSlots(List<String> slots) {
        this.closedSlots.addAll(slots);
    }

    /**
     * Remove horários da lista de horários fechados para este dia específico.
     *
     * @param slots Lista de horários a serem removidos.
     */
    public void removeClosedSlots(List<String> slots) {
        this.closedSlots.removeAll(slots);
    }

    /**
     * Limpa todos os horários fechados para este dia específico.
     */
    public void clearClosedSlots() {
        this.closedSlots.clear();
    }
}
