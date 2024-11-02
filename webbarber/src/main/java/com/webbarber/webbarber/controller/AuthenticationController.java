package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.infra.security.TokenService;
import com.webbarber.webbarber.repository.UserRepository;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.AuthenticationDTO;
import com.webbarber.webbarber.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthenticationController {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(TokenService tokenService, UserRepository userRepository,
                                    AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.tel(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        if(this.userRepository.findByLogin(data.tel()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.tel(), encryptedPassword);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
