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
    Optional<User> findByTel(String tel);
    @Query("SELECT u FROM user u WHERE u.tel = :login")
    UserDetails findByLogin(@Param("login") String login);
    @Nonnull
    List<User> findAll();
    @Query("SELECT new com.webbarber.webbarber.dto.UserInfoDTO(u.name, u.tel, u.amountBookedServices) FROM user u")
    List<UserInfoDTO> findAllUserDTOs();
    User findUserByTel(String tel);
}
