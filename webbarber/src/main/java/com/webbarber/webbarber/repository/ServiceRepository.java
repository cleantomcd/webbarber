package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.entity.Service;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Nonnull
    Optional<Service> findById(@Nonnull String id);
    Optional<List<Service>> findAllByName(@Nonnull String name);
    void delete(@Nonnull Service service);
    @Query("SELECT new com.webbarber.webbarber.dto.ServiceDTO(s.name, s.description, s.duration, s.priceInCents, s.active) FROM services s WHERE s.active = true")
    List<ServiceDTO> findAllByActiveTrue();
}
