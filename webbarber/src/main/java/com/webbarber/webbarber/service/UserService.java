package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.Barber;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.BarberRepository;
import com.webbarber.webbarber.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberRepository barberRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.barberRepository = barberRepository;
    }

    public void registerUser(@Valid RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        String formatedPhoneNumber = formatPhoneNumber(data.phone());
        User newUser = new User(data.name(), formatedPhoneNumber, encryptedPassword);
        userRepository.save(newUser);
    }

    private String formatPhoneNumber(String phone) {
        if(phone.startsWith("+55")) return phone;
        else return "+55" + phone;
    }

    public boolean userExists(String phone) {
        phone = formatPhoneNumber(phone);
        return userRepository.findByPhone(phone).isPresent();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public UserInfoDTO findUserByTel(String phone) {
        phone = formatPhoneNumber(phone);
        return userRepository.findUserByPhone(phone);
    }

    public void deleteUser(String id) {
        Optional<User> userOptional = getUserById(id);
        if(userOptional.isEmpty()) throw new UserNotFoundException("Usuário não encontrado");
        User user = userOptional.get();
        userRepository.delete(user);
    }

    public List<UserInfoDTO> getAllUsers() {
        return new ArrayList<>(userRepository.findAllUserDTOs());
    }

    public boolean existsUserById(String id) {
        return userRepository.existsById(id);
    }

    public String findIdByPhone(String phone) {
        return userRepository.findIdByPhone(phone);
    }
}
