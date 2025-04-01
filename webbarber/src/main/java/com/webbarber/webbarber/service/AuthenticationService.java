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

/**
 * Serviço responsável pela autenticação de usuários e barbeiros,
 * além de lidar com o registro de novos usuários.
 */
@Service
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BarberService barberService;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     *
     * @param tokenService Serviço responsável pela geração de tokens JWT.
     * @param userService Serviço que gerencia operações relacionadas ao usuário.
     * @param authenticationManager Gerenciador de autenticação do Spring Security.
     * @param barberService Serviço que gerencia operações relacionadas ao barbeiro.
     */
    public AuthenticationService(TokenService tokenService, UserService userService,
                                 AuthenticationManager authenticationManager, BarberService barberService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.barberService = barberService;
    }

    /**
     * Método responsável por autenticar o usuário ou barbeiro com base nas credenciais fornecidas.
     *
     * @param data DTO contendo as credenciais (telefone e senha) para autenticação.
     * @return Um objeto {@link LoginResponseDTO} contendo o token JWT gerado após a autenticação.
     * @throws InvalidRoleException Se o papel do usuário não for válido.
     */
    public LoginResponseDTO authenticate(AuthenticationDTO data) {
        // Formata o número de telefone
        String formatedPhone = formatPhoneNumber(data.phone());

        // Cria o token de autenticação com o telefone e a senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(formatedPhone, data.password());

        // Realiza a autenticação
        var auth = this.authenticationManager.authenticate(usernamePassword);
        Object principal = auth.getPrincipal();

        String phone;
        String role;

        // Verifica o tipo de usuário autenticado (usuário ou barbeiro)
        if (principal instanceof User user) {
            phone = user.getPhone();
            role = "ROLE_USER";  // Usuário comum
        } else if (principal instanceof Barber barber) {
            phone = barber.getPhone();
            role = "ROLE_ADMIN";  // Barbeiro (administrador)
        } else {
            throw new InvalidRoleException("Invalid role");  // Lança exceção se o papel for inválido
        }

        // Gera o token JWT
        var token = tokenService.generateToken(phone, role);
        return new LoginResponseDTO(token);  // Retorna o token em um DTO
    }

    /**
     * Método responsável por formatar o número de telefone para o padrão internacional com DDI (+55).
     *
     * @param tel Número de telefone fornecido pelo usuário.
     * @return O número de telefone formatado com o código do Brasil (+55).
     */
    private String formatPhoneNumber(String tel) {
        if(tel.startsWith("+55")) {
            return tel;  // Retorna o telefone se já estiver no formato correto
        } else {
            return "+55" + tel;  // Adiciona o código do Brasil (+55) caso necessário
        }
    }

    /**
     * Método responsável pelo registro de novos usuários.
     *
     * @param data DTO contendo os dados do usuário para registro.
     * @throws UserAlreadyExistsException Se o usuário ou barbeiro já estiver registrado.
     */
    public void register(RegisterDTO data) {
        validateRegister(data.phone());  // Valida se o número de telefone já está registrado
        userService.registerUser(data);   // Registra o novo usuário
    }

    /**
     * Método que valida se o número de telefone já está registrado no sistema.
     *
     * @param phone Número de telefone a ser validado.
     * @throws UserAlreadyExistsException Se o telefone já estiver registrado.
     */
    private void validateRegister(String phone) {
        // Verifica se o usuário ou barbeiro já existe
        if(userService.userExists(phone) || barberService.existsByPhone(phone)) {
            throw new UserAlreadyExistsException("Usuário já registrado");  // Lança exceção caso já exista
        }
    }
}
