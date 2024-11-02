package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.UserDTO;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDTO data) {
        checkIfUserExists(data.tel());
        User newUser = new User(data);
        userRepository.save(newUser);
    }

    private void checkIfUserExists(String tel) {
        userRepository.findByTel(tel).ifPresent(existingUser -> {
            throw new IllegalArgumentException("Usuário já cadastrado com esse telefone");
        });
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDetails> getUserByTel(String tel) {
        return userRepository.findByTel(tel);
    }


}
