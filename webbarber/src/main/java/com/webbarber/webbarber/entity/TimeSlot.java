package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * Entidade que representa o horário de atendimento de um barbeiro em um dia específico da semana.
 * Cada entrada define o horário de funcionamento do barbeiro para o período da manhã e da tarde.
 * A classe é mapeada para a tabela "timeslot" no banco de dados.
 */
@Table(name = "timeslot")
@Entity(name = "Timeslot")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String barberId;
    private int dayOfWeek; // Representa o dia da semana (1 = Domingo, 7 = Sábado)
    private LocalTime amStartTime; // Hora de início do turno da manhã
    private LocalTime amEndTime; // Hora de término do turno da manhã
    private LocalTime pmStartTime; // Hora de início do turno da tarde
    private LocalTime pmEndTime; // Hora de término do turno da tarde
    private int interval; // Intervalo entre os horários de agendamento

    /**
     * Construtor padrão necessário para a JPA.
     */
    public TimeSlot() {}

    /**
     * Construtor que cria um TimeSlot a partir de um objeto {@link StandardTimeSlotDTO}.
     *
     * @param barberId ID do barbeiro responsável pelo horário.
     * @param standardTimeSlotDTO Objeto DTO contendo os dados do horário.
     */
    public TimeSlot(String barberId, StandardTimeSlotDTO standardTimeSlotDTO) {
        this.barberId = barberId;
        this.dayOfWeek =  standardTimeSlotDTO.dayOfWeek();
        this.amStartTime = standardTimeSlotDTO.amStartTime();
        this.amEndTime = standardTimeSlotDTO.amEndTime();
        this.pmStartTime = standardTimeSlotDTO.pmStartTime();
        this.pmEndTime = standardTimeSlotDTO.pmEndTime();
        this.interval = standardTimeSlotDTO.interval();
    }

    /**
     * Obtém o ID do horário.
     *
     * @return O ID do horário.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o ID do barbeiro responsável por este horário.
     *
     * @return O ID do barbeiro.
     */
    public String getBarberId() {
        return barberId;
    }

    /**
     * Obtém o dia da semana em que este horário se aplica.
     *
     * @return O dia da semana (1 = Domingo, 7 = Sábado).
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Altera o dia da semana para este horário.
     *
     * @param dayOfWeek Novo dia da semana.
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Obtém a hora de início do turno da manhã.
     *
     * @return A hora de início do turno da manhã.
     */
    public LocalTime getAmStartTime() {
        return amStartTime;
    }

    /**
     * Altera a hora de início do turno da manhã.
     *
     * @param amStartTime Nova hora de início para o turno da manhã.
     */
    public void setAmStartTime(LocalTime amStartTime) {
        this.amStartTime = amStartTime;
    }

    /**
     * Obtém a hora de término do turno da manhã.
     *
     * @return A hora de término do turno da manhã.
     */
    public LocalTime getAmEndTime() {
        return amEndTime;
    }

    /**
     * Altera a hora de término do turno da manhã.
     *
     * @param amEndTime Nova hora de término para o turno da manhã.
     */
    public void setAmEndTime(LocalTime amEndTime) {
        this.amEndTime = amEndTime;
    }

    /**
     * Obtém a hora de início do turno da tarde.
     *
     * @return A hora de início do turno da tarde.
     */
    public LocalTime getPmStartTime() {
        return pmStartTime;
    }

    /**
     * Altera a hora de início do turno da tarde.
     *
     * @param pmStartTime Nova hora de início para o turno da tarde.
     */
    public void setPmStartTime(LocalTime pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    /**
     * Obtém a hora de término do turno da tarde.
     *
     * @return A hora de término do turno da tarde.
     */
    public LocalTime getPmEndTime() {
        return pmEndTime;
    }

    /**
     * Altera a hora de término do turno da tarde.
     *
     * @param pmEndTime Nova hora de término para o turno da tarde.
     */
    public void setPmEndTime(LocalTime pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    /**
     * Obtém o intervalo entre os horários de agendamento.
     *
     * @return O intervalo, em minutos.
     */
    public int getInterval() {
        return this.interval;
    }

    /**
     * Altera o intervalo entre os horários de agendamento.
     *
     * @param interval O novo intervalo, em minutos.
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }
}
