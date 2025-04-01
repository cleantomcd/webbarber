package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.StandardTimeSlotDTO;
import com.webbarber.webbarber.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link TimeSlot}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. Define consultas personalizadas utilizando JPQL.
 */
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {

    /**
     * Busca os detalhes do horário padrão de um barbeiro para um dia específico da semana.
     *
     * @param barberId ID do barbeiro.
     * @param dayOfWeek O número do dia da semana, onde 1 é segunda-feira, 7 é domingo.
     * @return Um DTO contendo as informações sobre o horário do barbeiro para o dia da semana específico.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.StandardTimeSlotDTO(" +
            "t.dayOfWeek, t.amStartTime, t.amEndTime, t.pmStartTime, t.pmEndTime, t.interval) " +
            "FROM Timeslot t WHERE t.barberId = :barberId AND t.dayOfWeek = :day")
    StandardTimeSlotDTO findByBarberIdAndDayOfWeek(@Param("barberId") String barberId,
                                                   @Param("day") int dayOfWeek);

    /**
     * Busca um slot de horário específico para um barbeiro e um dia da semana.
     * Retorna um {@link Optional} que pode conter o slot de horário encontrado ou estar vazio.
     *
     * @param barberId ID do barbeiro.
     * @param dayOfWeek O número do dia da semana, onde 1 é segunda-feira, 7 é domingo.
     * @return Um {@link Optional} contendo o slot de horário, caso encontrado.
     */
    @Query("SELECT t FROM Timeslot t WHERE t.barberId = :barberId AND t.dayOfWeek = :dayOfWeek")
    Optional<TimeSlot> optionalFindByBarberIdAndDayOfWeek(@Param("barberId") String barberId,
                                                          @Param("dayOfWeek") int dayOfWeek);

}
