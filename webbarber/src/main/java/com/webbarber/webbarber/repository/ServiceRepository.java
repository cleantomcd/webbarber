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

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByBarberIdAndId(String barberId, String id);

    void delete(@Nonnull Service service);

    @Query("SELECT new com.webbarber.webbarber.dto.ServiceDTO" +
            "(s.name, s.description, s.duration, s.priceInCents, s.active)" +
            " FROM Service s WHERE s.barberId = :barberId AND s.active = true")
    List<ServiceDTO> findAllByBarberIdAndActiveTrue(String barberId);

    boolean existsByBarberIdAndId(String barberId, String id);

    @Query("SELECT s.duration FROM Service s WHERE s.barberId = :barberId AND s.id = :id")
    int getDurationByBarberIdAndId(@Param("barberId") String barberId, @Param("id") String id);
}
