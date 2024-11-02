package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.repository.UserRepository;
import jakarta.validation.Valid;
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

    public void registerUser(@Valid RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.name(), data.tel(), encryptedPassword);
        userRepository.save(newUser);
    }

    public boolean userExists(String tel) {
        return userRepository.findByTel(tel).isPresent();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserInfoDTO> getUserInfo(String tel) {
        Optional<User> user = getUserByTel(tel);
        return user.map(userInfo -> new UserInfoDTO(userInfo.getName(), userInfo.getTel()));
    }

    public Optional<User> getUserByTel(String tel) {
        return userRepository.findByTel(tel);
    }


}
