package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.exception.UserAlreadyExistsException;
import com.webbarber.webbarber.service.AuthenticationService;
import jakarta.transaction.Transactional;
import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.AuthenticationDTO;
import com.webbarber.webbarber.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsável pelo processo de autenticação e registro de usuários.
 * Fornece endpoints para login e registro de novos usuários.
 */
@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Construtor para injeção do serviço de autenticação.
     *
     * @param authenticationService Serviço de autenticação a ser utilizado pelo controller.
     */
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint para autenticação de usuários.
     *
     * @param data Objeto contendo credenciais do usuário para autenticação.
     * @return ResponseEntity contendo o token de autenticação caso o login seja bem-sucedido.
     */
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var loginResponse = authenticationService.authenticate(data);
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Endpoint para registro de novos usuários.
     *
     * @param data Objeto contendo as informações do usuário a ser registrado.
     * @return ResponseEntity informando o sucesso do registro.
     */
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        authenticationService.register(data);
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }

    /**
     * Manipula exceções quando um usuário já existe no sistema.
     *
     * @param ex Exceção lançada quando um usuário já está cadastrado.
     * @return ResponseEntity com status de conflito e a mensagem de erro correspondente.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Manipula exceções de autenticação interna, como credenciais inválidas.
     *
     * @return ResponseEntity com status de conflito e mensagem de erro correspondente.
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthenticationServiceException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Número de telefone e/ou senha inválida");
    }
}
