package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{tel}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable String tel) {
        UserInfoDTO userInfo = userService.getUserInfo(tel);
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


}
