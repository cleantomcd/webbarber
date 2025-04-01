package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.entity.Service;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório que gerencia as operações de persistência para a entidade {@link Service}.
 * Extende {@link JpaRepository} para fornecer acesso a funcionalidades de CRUD (Create, Read, Update, Delete)
 * sem a necessidade de implementação adicional. A interface também define consultas personalizadas utilizando JPQL (Java Persistence Query Language).
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    /**
     * Busca um serviço específico de um barbeiro pelo ID do barbeiro e ID do serviço.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço.
     * @return {@link Optional} contendo o serviço se encontrado, caso contrário, retorna {@link Optional#empty()}.
     */
    Optional<Service> findByBarberIdAndId(String barberId, String id);

    /**
     * Deleta um serviço do banco de dados.
     *
     * @param service Instância de {@link Service} a ser deletada.
     */
    void delete(@Nonnull Service service);

    /**
     * Busca todos os serviços ativos de um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @return Lista de {@link ServiceDTO} com as informações dos serviços ativos do barbeiro.
     */
    @Query("SELECT new com.webbarber.webbarber.dto.ServiceDTO" +
            "(s.name, s.description, s.duration, s.priceInCents, s.active)" +
            " FROM Service s WHERE s.barberId = :barberId AND s.active = true")
    List<ServiceDTO> findAllByBarberIdAndActiveTrue(String barberId);

    /**
     * Verifica se existe um serviço com o ID especificado para o barbeiro fornecido.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço.
     * @return true se o serviço existir para o barbeiro, caso contrário, retorna false.
     */
    boolean existsByBarberIdAndId(String barberId, String id);

    /**
     * Busca a duração de um serviço específico de um barbeiro pelo ID do barbeiro e ID do serviço.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço.
     * @return Duração do serviço em minutos.
     */
    @Query("SELECT s.duration FROM Service s WHERE s.barberId = :barberId AND s.id = :id")
    int getDurationByBarberIdAndId(@Param("barberId") String barberId, @Param("id") String id);
}
