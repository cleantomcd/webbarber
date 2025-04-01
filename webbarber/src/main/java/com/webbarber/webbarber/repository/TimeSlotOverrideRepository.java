package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.TimeSlotOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link TimeSlotOverride}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. A interface também define consultas personalizadas utilizando JPQL e SQL nativo.
 */
@Repository
public interface TimeSlotOverrideRepository extends JpaRepository<TimeSlotOverride, String> {

    /**
     * Busca uma sobrecarga de horário para um barbeiro em uma data específica.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do dia que se deseja verificar a sobrecarga de horários.
     * @return {@link Optional} contendo a sobrecarga de horário, caso encontrada, ou {@link Optional#empty()} caso não exista.
     */
    Optional<TimeSlotOverride> findByBarberIdAndDate(String barberId, LocalDate date);

    /**
     * Busca uma sobrecarga de horário para um barbeiro em uma data específica e retorna como um DTO.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do dia que se deseja verificar a sobrecarga de horários.
     * @return A sobrecarga de horário, representada por um DTO.
     */
    TimeSlotOverride findDTOByBarberIdAndDate(String barberId, LocalDate date);

    /**
     * Busca os horários fechados para um barbeiro em um dia específico, verificando se o horário específico está
     * incluído nos slots fechados para essa data.
     *
     * @param barberId ID do barbeiro.
     * @param time Hora a ser verificada nos slots fechados.
     * @return Lista de IDs de slots fechados que contêm o horário fornecido.
     */
    @Query(value = "SELECT ts.closed_slots " +
            "FROM TimeslotOverride t " +
            "JOIN timeslot_override_closed_slots ts " +
            "ON t.id = ts.timeslot_override_id " +
            "WHERE t.barberId = :barberId AND :time = ANY(SELECT ts.closed_slots FROM timeslot_override_closed_slots ts )",
            nativeQuery = true)
    List<String> findClosedSlotsContainingTime(@Param("barberId") String barberId, @Param("time") String time);
}
