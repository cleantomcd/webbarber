package com.webbarber.webbarber.service;

import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String tel) throws UserNotFoundException {
        return userRepository.findByLogin(tel);
    }
}
