package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{tel}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable String tel) {
        return ResponseEntity.ok(userService.findUserByTel(tel));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }

    @GetMapping()
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }



}
