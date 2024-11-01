package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.UserDTO;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.service.UserService;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/join")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO data) {
        this.userService.registerUser(data);
        return ResponseEntity.ok("Cadastro realizado com sucesso");
    }

}
