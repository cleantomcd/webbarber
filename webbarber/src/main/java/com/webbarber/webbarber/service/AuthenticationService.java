package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public AuthenticationService(TokenService tokenService, UserService userService,
                                    AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponseDTO authenticate(AuthenticationDTO data) {
        String formatedPhone = formatPhoneNumber(data.tel());
        var usernamePassword = new UsernamePasswordAuthenticationToken(formatedPhone, data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    private String formatPhoneNumber(String tel) {
        if(tel.startsWith("+55")) return tel;
        else return "+55" + tel;
    }

    public void register(RegisterDTO data) {
        if(userService.userExists(data.tel())) throw new UserAlreadyExistsException("Usuário já registrado");
        userService.registerUser(data);
    }


}
