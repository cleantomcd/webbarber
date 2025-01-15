package com.webbarber.webbarber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbarber.webbarber.dto.AuthenticationDTO;
import com.webbarber.webbarber.dto.LoginResponseDTO;
import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.exception.UserAlreadyExistsException;
import com.webbarber.webbarber.service.AuthenticationService;
import com.webbarber.webbarber.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @Test
    void registerShouldReturnOkWhenCredentialsAreValid() throws Exception {
        RegisterDTO data = new RegisterDTO("user", "83999999991", "12345678");
        Mockito.doNothing().when(authenticationService).register(Mockito.any(RegisterDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk());
    }

    @Test
    void registerShouldReturnBadRequestWhenCredentialsAreInvalid() throws Exception {
        RegisterDTO data = new RegisterDTO("","83999999991", "12345678");
        Mockito.doNothing().when(authenticationService).register(Mockito.any(RegisterDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerShouldReturnConflictWhenPhoneAlreadyRegistered() throws Exception {
        RegisterDTO data = new RegisterDTO("user", "83999999991", "12345678");

        // Configura o mock para simular que o telefone já está registrado
        Mockito.when(userService.existsUserById("83999999991")).thenReturn(true);

        // Configura o mock para lançar a exceção no serviço
        Mockito.doThrow(new UserAlreadyExistsException("Phone number already registered."))
                .when(authenticationService).register(Mockito.any(RegisterDTO.class));

        // Executa a requisição e verifica o resultado
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isConflict()) // Verifica se o status é 409 CONFLICT
                .andExpect(jsonPath("$.message").value("Phone number already registered."));
    }




}

