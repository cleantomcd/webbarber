package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.entity.Barber;
import com.webbarber.webbarber.exception.InvalidRoleException;
import com.webbarber.webbarber.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.infra.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import com.webbarber.webbarber.dto.AuthenticationDTO;
import com.webbarber.webbarber.dto.LoginResponseDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BarberService barberService;

    public AuthenticationService(TokenService tokenService, UserService userService,
                                 AuthenticationManager authenticationManager, BarberService barberService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.barberService = barberService;
    }

    public LoginResponseDTO authenticate(AuthenticationDTO data) {
        String formatedPhone = formatPhoneNumber(data.phone());
        var usernamePassword = new UsernamePasswordAuthenticationToken(formatedPhone, data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        Object principal = auth.getPrincipal();
        String phone;
        String role;

        if (principal instanceof User user) {
            phone = user.getPhone();
            role = "ROLE_USER";
        } else if (principal instanceof Barber barber) {
            phone = barber.getPhone();
            role = "ROLE_ADMIN";

        }
        else {
            throw new InvalidRoleException("Invalid role");
        }

        var token = tokenService.generateToken(phone, role);
        return new LoginResponseDTO(token);
    }

    private String formatPhoneNumber(String tel) {
        if(tel.startsWith("+55")) return tel;
        else return "+55" + tel;
    }

    public void register(RegisterDTO data) {
        validateRegister(data.phone());
        userService.registerUser(data);
    }

    private void validateRegister(String phone) {
        if(userService.userExists(phone) || barberService.existsByPhone(phone))
            throw new UserAlreadyExistsException("Usuário já registrado");
    }


}
