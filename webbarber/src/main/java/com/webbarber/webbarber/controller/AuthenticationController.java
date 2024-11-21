package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.infra.security.TokenService;
import com.webbarber.webbarber.service.AuthenticationService;
import com.webbarber.webbarber.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.AuthenticationDTO;
import com.webbarber.webbarber.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var loginResponse = authenticationService.authenticate(data);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        return authenticationService.register(data);
    }
}
