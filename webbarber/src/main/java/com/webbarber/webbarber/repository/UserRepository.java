package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link User}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. Define consultas personalizadas utilizando JPQL.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário.
     * @return Um {@link Optional} contendo o usuário, caso encontrado.
     */
    @Nonnull
    Optional<User> findById(@Nonnull String id);

    /**
     * Busca um usuário pelo número de telefone.
     *
     * @param phone Número de telefone do usuário.
     * @return Um {@link Optional} contendo o usuário, caso encontrado.
     */
    Optional<User> findByPhone(String phone);

    /**
     * Busca um usuário pelo seu número de telefone para fins de autenticação.
     *
     * @param login Número de telefone do usuário.
     * @return Um objeto {@link UserDetails} contendo os dados do usuário para autenticação.
     */
    @Query("SELECT u FROM User u WHERE u.phone = :login")
    UserDetails findByLogin(@Param("login") String login);

    /**
     * Recupera todos os usuários e os converte em uma lista de DTOs com informações básicas (nome, telefone e quantidade de serviços agendados).
     *
     * @return Uma lista de {@link UserInfoDTO} contendo informações básicas de todos os usuários.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.UserInfoDTO(u.name, u.phone, u.amountBookedServices) FROM User u")
    List<UserInfoDTO> findAllUserDTOs();

    /**
     * Recupera um usuário específico pelo número de telefone e retorna suas informações básicas em um DTO.
     *
     * @param phone Número de telefone do usuário.
     * @return Um {@link UserInfoDTO} contendo o nome, telefone e a quantidade de serviços agendados do usuário.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.UserInfoDTO(u.name, u.phone, u.amountBookedServices) FROM User u WHERE u.phone = :phone")
    UserInfoDTO findUserByPhone(@Param("phone") String phone);

    /**
     * Verifica se um usuário com o ID fornecido existe.
     *
     * @param id ID do usuário.
     * @return Um valor booleano indicando se o usuário existe ou não.
     */
    boolean existsById(String id);

    /**
     * Busca o ID de um usuário pelo número de telefone.
     *
     * @param phone Número de telefone do usuário.
     * @return O ID do usuário.
     */
    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    String findIdByPhone(@Param("phone") String phone);
}
