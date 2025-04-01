package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.RequestBookingDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidade que representa uma reserva (agendamento) feita por um usuário com um barbeiro, incluindo o serviço, o horário de início e término, e a data da reserva.
 * Esta classe é mapeada para a tabela "bookings" no banco de dados.
 */
@Entity(name = "Booking")
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String barberId;
    private String userId;
    private String serviceId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Construtor padrão necessário para a JPA.
     */
    public Booking() {}

    /**
     * Construtor utilizado para criar uma nova reserva a partir dos dados fornecidos em {@link RequestBookingDTO}.
     * O horário de término é calculado externamente e passado como argumento.
     *
     * @param userId ID do usuário que está fazendo a reserva.
     * @param data Dados de reserva fornecidos no {@link RequestBookingDTO}.
     * @param endTime Hora de término da reserva.
     */
    public Booking(String userId, RequestBookingDTO data, LocalTime endTime) {
        this.barberId = data.barberId();
        this.userId = userId;
        this.serviceId = data.serviceId();
        this.startTime = data.startTime();
        this.endTime = endTime;
        this.date = data.date();
    }

    /**
     * Construtor utilizado para criar uma nova reserva diretamente com os dados fornecidos.
     *
     * @param barberId ID do barbeiro.
     * @param userId ID do usuário.
     * @param serviceId ID do serviço.
     * @param date Data da reserva.
     * @param startTime Hora de início da reserva.
     * @param endTime Hora de término da reserva.
     */
    public Booking(String barberId, String userId, String serviceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.barberId = barberId;
        this.endTime = endTime;
        this.startTime = startTime;
        this.date = date;
        this.serviceId = serviceId;
        this.userId = userId;
    }

    /**
     * Obtém o ID do barbeiro responsável pela reserva.
     *
     * @return O ID do barbeiro.
     */
    public String getBarberId() {
        return barberId;
    }

    /**
     * Obtém o ID da reserva.
     *
     * @return O ID da reserva.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o ID do usuário que fez a reserva.
     *
     * @return O ID do usuário.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Obtém o ID do serviço associado à reserva.
     *
     * @return O ID do serviço.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Obtém o horário de início da reserva.
     *
     * @return O horário de início da reserva.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Obtém o horário de término da reserva.
     *
     * @return O horário de término da reserva.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Obtém a data da reserva.
     *
     * @return A data da reserva.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Adiciona uma quantidade de minutos ao horário de início da reserva.
     *
     * @param minutes Quantidade de minutos a ser adicionada ao horário de início.
     */
    public void addStartTime(int minutes) {
        this.startTime = this.startTime.plusMinutes(minutes);
    }
}
