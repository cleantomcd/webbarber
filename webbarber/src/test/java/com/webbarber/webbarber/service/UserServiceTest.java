package com.webbarber.webbarber.service;

import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final String testPhone = "+5583993250033";
    private final String testName = "User";
    private final String encodedPassword = "encodedPassword123";

    @Test
    void whenValidUserIdThenUserShouldBeFound() {
        User mockUser = new User(testName, testPhone, encodedPassword);

        when(userService.getUserById(testPhone)).thenReturn(Optional.of(mockUser));

        Optional<User> found = userService.getUserById(testPhone);

        assertTrue(found.isPresent(), "O usuário deveria estar presente");
        assertEquals(testName, found.get().getName());
        assertEquals(testPhone, found.get().getPhone());

        verify(userService, times(1)).getUserById(testPhone);
    }

    @Test
    void whenInvalidUserIdThenUserShouldNotBeFound() {
        String notRegisteredPhone = "+552389823682";
        Optional<User> found = userService.getUserById(notRegisteredPhone); //invalid phone
        assertFalse(found.isPresent(), "Usuário inválido");
        verify(userService, times(1)).getUserById(notRegisteredPhone);
    }

    @Test
    void shouldReturnTrueWhenUserExistsInDB() {
        User mockUser = new User(testName, testPhone, encodedPassword);

        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(mockUser));
        assertTrue(userService.userExists(testPhone), "O usuário deveria estar presente");


        verify(userRepository, times(1)).findByPhone(testPhone);
    }
}