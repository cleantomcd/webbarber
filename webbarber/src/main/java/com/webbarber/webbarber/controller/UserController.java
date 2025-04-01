package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável pela gestão de usuários dentro do sistema.
 */
@RestController
@RequestMapping("/barber/users")
public class UserController {
    private final UserService userService;

    /**
     * Construtor da classe {@code UserController}.
     *
     * @param userService Serviço responsável pela manipulação de usuários.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtém as informações de um usuário a partir do número de telefone.
     *
     * @param phone Número de telefone do usuário.
     * @return {@code ResponseEntity} contendo as informações do usuário.
     */
    @GetMapping("/{phone}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable String phone) {
        return ResponseEntity.ok(userService.findUserByTel(phone));
    }

    /**
     * Deleta um usuário com base no seu ID.
     *
     * @param id Identificador único do usuário a ser deletado.
     * @return {@code ResponseEntity} contendo uma mensagem de sucesso.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }

    /**
     * Obtém a lista de todos os usuários cadastrados no sistema.
     *
     * @param authentication Objeto de autenticação do usuário que realiza a requisição.
     * @return {@code ResponseEntity} contendo uma lista de usuários cadastrados.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Manipula exceções lançadas quando um usuário não é encontrado.
     *
     * @param ex Exceção de usuário não encontrado.
     * @return {@code ResponseEntity} contendo a mensagem de erro e o status HTTP correspondente.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
