package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.User;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(@Valid RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        String formatedPhoneNumber = formatPhoneNumber(data.tel());
        User newUser = new User(data.name(), formatedPhoneNumber, encryptedPassword);
        userRepository.save(newUser);
    }

    private String formatPhoneNumber(String tel) {
        if(tel.startsWith("+55")) return tel;
        else return "+55" + tel;
    }

    public void verifyIfUserExists(String tel) {
        if(userRepository.findByTel(tel).isEmpty()) throw new IllegalArgumentException();
    }

    public boolean userExists(String tel) {
        return userRepository.findByTel(tel).isPresent();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User findUserByTel(String tel) {
        tel = formatPhoneNumber(tel);
        return userRepository.findUserByTel(tel);
    }

    public UserInfoDTO getUserInfo(String tel) {
        tel = formatPhoneNumber(tel);
        User user = findUserByTel(tel);
        return new UserInfoDTO(user.getName(), user.getTel(), user.getAmountBookedServices());
    }

    public void deleteUser(String id) {
        Optional<User> userOptional = getUserById(id);
        if(userOptional.isEmpty()) throw new IllegalArgumentException();
        User user = userOptional.get();
        userRepository.delete(user);
    }

    public List<UserInfoDTO> getAllUsers() {
        return new ArrayList<>(userRepository.findAllUserDTOs());
    }


}
