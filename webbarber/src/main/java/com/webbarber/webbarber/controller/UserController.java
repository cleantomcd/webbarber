package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{tel}")
    public ResponseEntity<Void> getUserInfo(@PathVariable String tel) {
        Optional<UserInfoDTO> userInfo = userService.getUserInfo(tel);
        return ResponseEntity.ok().build();
    }



}
