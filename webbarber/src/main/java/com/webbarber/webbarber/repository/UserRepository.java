package com.webbarber.webbarber.repository;

import com.webbarber.webbarber.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Nonnull
    Optional<User> findById(@Nonnull Long id);
    Optional<User> findByTel(String tel);
    UserDetails findByLogin(String login);
    @Nonnull
    List<User> findAll();
    User findUserByTel(String tel);
}
