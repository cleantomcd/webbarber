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

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Nonnull
    Optional<User> findById(@Nonnull String id);

    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.phone = :login")
    UserDetails findByLogin(@Param("login") String login);

    @Query("SELECT new com.webbarber.webbarber.dto.UserInfoDTO(u.name, u.phone, u.amountBookedServices) FROM User u")
    List<UserInfoDTO> findAllUserDTOs();

    @Query("SELECT new com.webbarber.webbarber.dto.UserInfoDTO(u.name, u.phone, u.amountBookedServices) FROM User u WHERE u.phone = :phone")
    UserInfoDTO findUserByPhone(@Param("phone") String phone);

    boolean existsById(String id);

    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    String findIdByPhone(@Param("phone") String phone);
}
