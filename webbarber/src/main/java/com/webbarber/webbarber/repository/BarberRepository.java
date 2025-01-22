package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface BarberRepository extends JpaRepository<Barber, Long> {

    @Query("SELECT b FROM Barber b WHERE b.phone = :login")
    UserDetails findByLogin(@Param("login") String login);

    boolean existsByPhone(String phone);

    @Query("SELECT b.id FROM Barber b WHERE b.phone = :phone")
    String findIdByPhone(@Param("phone") String phone);


}
