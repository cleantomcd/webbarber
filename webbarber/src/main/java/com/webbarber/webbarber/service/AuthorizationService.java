package com.webbarber.webbarber.service;

import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.BarberRepository;
import com.webbarber.webbarber.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BarberRepository barberRepository;

    public AuthorizationService(UserRepository userRepository, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String phone) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if(optionalUser.isPresent()) return userRepository.findByLogin(phone);
        return barberRepository.findByLogin(phone);
    }

}
