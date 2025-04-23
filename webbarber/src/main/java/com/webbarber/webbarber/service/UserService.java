package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.UserInfoDTO;
import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.UserRepository;
import com.webbarber.webbarber.repository.BarberRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço responsável pelas tarefas de gerenciamento de usuários, como registro, recuperação e exclusão.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor da classe UserService.
     *
     * @param userRepository o repositório utilizado para a persistência dos dados de usuário
     * @param passwordEncoder o codificador de senha utilizado para criptografar as senhas dos usuários
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param data os dados do usuário a ser registrado
     */
    public void registerUser(@Valid RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        String formatedPhoneNumber = formatPhoneNumber(data.phone());
        User newUser = new User(data.name(), formatedPhoneNumber, encryptedPassword);
        userRepository.save(newUser);
    }

    /**
     * Formata o número de telefone do usuário para o formato internacional.
     *
     * @param phone o número de telefone a ser formatado
     * @return o número de telefone formatado
     */
    private String formatPhoneNumber(String phone) {
        if(phone.startsWith("+55")) return phone;
        else return "+55" + phone;
    }

    /**
     * Verifica se um usuário já existe no sistema com o número de telefone fornecido.
     *
     * @param phone o número de telefone do usuário
     * @return true se o usuário existir, caso contrário, false
     */
    public boolean userExists(String phone) {
        phone = formatPhoneNumber(phone);
        return userRepository.findByPhone(phone).isPresent();
    }

    /**
     * Recupera um usuário pelo seu ID.
     *
     * @param id o ID do usuário
     * @return um Optional contendo o usuário encontrado ou vazio se não encontrado
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Encontra as informações de um usuário pelo seu número de telefone.
     *
     * @param phone o número de telefone do usuário
     * @return as informações do usuário, encapsuladas em um DTO
     */
    public UserInfoDTO findUserByTel(String phone) {
        phone = formatPhoneNumber(phone);
        return userRepository.findUserByPhone(phone);
    }

    /**
     * Exclui um usuário do sistema com base no seu ID.
     *
     * @param id o ID do usuário a ser excluído
     * @throws UserNotFoundException se o usuário com o ID fornecido não for encontrado
     */
    public void deleteUser(String id) {
        Optional<User> userOptional = getUserById(id);
        if(userOptional.isEmpty()) throw new UserNotFoundException("Usuário não encontrado");
        User user = userOptional.get();
        userRepository.delete(user);
    }

    /**
     * Recupera todos os usuários do sistema.
     *
     * @return uma lista contendo todas as informações dos usuários no formato DTO
     */
    public List<UserInfoDTO> getAllUsers() {
        return new ArrayList<>(userRepository.findAllUserDTOs());
    }

    /**
     * Verifica se um usuário existe no sistema com base no seu ID.
     *
     * @param id o ID do usuário
     * @return true se o usuário existir, caso contrário, false
     */
    public boolean existsUserById(String id) {
        return userRepository.existsById(id);
    }

    /**
     * Encontra o ID de um usuário com base no seu número de telefone.
     *
     * @param phone o número de telefone do usuário
     * @return o ID do usuário correspondente
     */
    public String findIdByPhone(String phone) {
        return userRepository.findIdByPhone(phone);
    }
}
