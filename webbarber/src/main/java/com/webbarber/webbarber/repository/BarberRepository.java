package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link Barber}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. A interface também define consultas personalizadas usando JPQL (Java Persistence Query Language).
 */
public interface BarberRepository extends JpaRepository<Barber, Long> {

    /**
     * Consulta para encontrar um barbeiro com base no número de telefone (utilizado como login).
     *
     * @param login Número de telefone do barbeiro (login).
     * @return {@link UserDetails} que contém as informações do barbeiro, como telefone e senha.
     */
    @Query("SELECT b FROM Barber b WHERE b.phone = :login")
    UserDetails findByLogin(@Param("login") String login);

    /**
     * Verifica se já existe um barbeiro com o número de telefone informado.
     *
     * @param phone Número de telefone do barbeiro.
     * @return true se um barbeiro com o número de telefone já existir, false caso contrário.
     */
    boolean existsByPhone(String phone);

    /**
     * Consulta para encontrar o ID do barbeiro com base no número de telefone.
     *
     * @param phone Número de telefone do barbeiro.
     * @return O ID do barbeiro correspondente ao número de telefone.
     */
    @Query("SELECT b.id FROM Barber b WHERE b.phone = :phone")
    String findIdByPhone(@Param("phone") String phone);
}
