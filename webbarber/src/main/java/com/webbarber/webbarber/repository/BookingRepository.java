package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.entity.Booking;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link Booking}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. A interface também define consultas personalizadas utilizando JPQL (Java Persistence Query Language).
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Busca um agendamento pelo ID.
     *
     * @param id ID do agendamento.
     * @return {@link Optional} contendo o agendamento se encontrado, caso contrário, retorna {@link Optional#empty()}.
     */
    @Nonnull
    Optional<Booking> findById(@Nonnull String id);

    /**
     * Verifica se já existe um agendamento para o barbeiro em uma data e horário específicos.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do agendamento.
     * @param startTime Horário de início do agendamento.
     * @return {@link Optional} contendo o agendamento se encontrado, caso contrário, retorna {@link Optional#empty()}.
     */
    @Query("SELECT new com.webbarber.webbarber.entity.Booking(b.userId, b.barberId, b.serviceId, b.date, b.startTime, b.endTime) FROM Booking b WHERE b.barberId = :barberId AND b.date = :date AND b.startTime = :startTime")
    Optional<Booking> findByBarberIdAndDateAndStartTime(@Param("barberId") String barberId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    /**
     * Busca todos os horários de início de agendamento para um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do agendamento.
     * @return Lista de horários de início dos agendamentos.
     */
    @Query("SELECT b.startTime FROM Booking b WHERE b.barberId = :barberId AND b.date = :date")
    List<LocalTime> findStartTimesByBarberIdAndDate(@Param("barberId") String barberId, @Param("date") LocalDate date);

    /**
     * Verifica se existe algum agendamento conflitante para o barbeiro em uma data e horário específicos.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do agendamento.
     * @param startTime Horário de início do novo agendamento.
     * @return {@link Optional} contendo o agendamento conflitante, caso exista, caso contrário, retorna {@link Optional#empty()}.
     */
    @Query("SELECT b FROM Booking b WHERE b.barberId = :barberId AND b.date = :date AND :startTime >= b.startTime AND :startTime < b.endTime")
    Optional<Booking> findConflictingBooking(@Param("barberId") String barberId, @Param("date") LocalDate date,
                                             @Param("startTime") LocalTime startTime);

    /**
     * Busca todos os agendamentos para um barbeiro em uma data específica, incluindo informações adicionais dos usuários e serviços.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do agendamento.
     * @return Lista de {@link BookingInfoDTO} com as informações dos agendamentos, incluindo o nome do usuário, nome do barbeiro,
     *         nome do serviço, data, horário de início e horário de término.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.BookingInfoDTO(u.name, br.name, s.name, b.date, b.startTime, b.endTime) " +
            "FROM Booking b " +
            "JOIN User u ON b.userId = u.id " +
            "JOIN Service s ON b.serviceId = s.id " +
            "JOIN Barber br ON b.barberId = br.id " +
            "WHERE b.date = :date AND br.id = :barberId " +
            "ORDER BY b.date, b.startTime")
    List<BookingInfoDTO> findAllByBarberIdAndDate(@Param("barberId") String barberId, @Param("date") LocalDate date);

    /**
     * Busca todos os agendamentos de um usuário específico para um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @param userId ID do usuário.
     * @return Lista de {@link BookingDTO} contendo as informações dos agendamentos realizados.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.BookingDTO(b.userId, b.barberId, b.serviceId, b.date, b.startTime, b.endTime) FROM Booking b WHERE b.barberId = :barberId AND b.userId = :userId")
    List<BookingDTO> findAllByBarberIdAndUserId(@Param("barberId") String barberId, @Param("userId") String userId);
}
