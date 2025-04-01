package com.webbarber.webbarber.service;

import com.webbarber.webbarber.entity.User;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.BarberRepository;
import com.webbarber.webbarber.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviço responsável pela autenticação e carregamento de detalhes do usuário
 * para o processo de autenticação no Spring Security.
 */
@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BarberRepository barberRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     *
     * @param userRepository Repositório para operações relacionadas a usuários.
     * @param barberRepository Repositório para operações relacionadas a barbeiros.
     */
    public AuthorizationService(UserRepository userRepository, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
    }

    /**
     * Método responsável por carregar os detalhes do usuário (seja usuário comum ou barbeiro)
     * com base no número de telefone fornecido.
     *
     * @param phone Número de telefone do usuário ou barbeiro.
     * @return Os detalhes do usuário (implementação de {@link UserDetails}).
     * @throws UserNotFoundException Se nenhum usuário ou barbeiro for encontrado com o número de telefone fornecido.
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isPresent()) {
            return userRepository.findByLogin(phone);
        }

        var barber = barberRepository.findByLogin(phone);
        if (barber != null) {
            return barber;
        }

        throw new UserNotFoundException("Usuário ou barbeiro não encontrado");
    }
}
