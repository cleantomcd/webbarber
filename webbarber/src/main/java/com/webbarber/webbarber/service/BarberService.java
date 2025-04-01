package com.webbarber.webbarber.service;

import com.webbarber.webbarber.repository.BarberRepository;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela manipulação de dados relacionados aos barbeiros.
 * Ele utiliza o repositório BarberRepository para verificar a existência de barbeiros
 * e realizar outras operações relacionadas aos barbeiros.
 */
@Service
public class BarberService {

    private final BarberRepository barberRepository;

    /**
     * Construtor que inicializa o serviço com o repositório de barbeiros.
     *
     * @param barberRepository O repositório de barbeiros para realizar operações de acesso ao banco de dados.
     */
    public BarberService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    /**
     * Verifica se já existe um barbeiro registrado no sistema com o número de telefone fornecido.
     *
     * @param phone O número de telefone do barbeiro.
     * @return Verdadeiro se o barbeiro existe, falso caso contrário.
     */
    public boolean existsByPhone(String phone) {
        return barberRepository.existsByPhone(phone);
    }

    /**
     * Recupera o ID do barbeiro com base no número de telefone fornecido.
     *
     * @param phone O número de telefone do barbeiro.
     * @return O ID do barbeiro associado ao número de telefone fornecido.
     */
    public String findIdByPhone(String phone) {
        return barberRepository.findIdByPhone(phone);
    }
}
