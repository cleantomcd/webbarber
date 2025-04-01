package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.entity.Service;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar os serviços oferecidos pelos barbeiros.
 * Oferece métodos para criar, atualizar, excluir, ativar/desativar e consultar serviços.
 */
@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    /**
     * Construtor para inicializar o serviço com o repositório de serviços.
     *
     * @param serviceRepository Repositório para manipulação dos serviços.
     */
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Cria um novo serviço para um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @param service DTO contendo os dados do serviço.
     */
    public void createService(String barberId, ServiceDTO service) {
        Service newService = new Service(barberId, service);
        serviceRepository.save(newService);
    }

    /**
     * Atualiza os dados de um serviço específico de um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço a ser atualizado.
     * @param updatedService DTO contendo os dados atualizados do serviço.
     */
    public void updateService(String barberId, String id, ServiceDTO updatedService) {
        Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
        if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
        Service service = optionalService.get();
        updateServiceAttributes(service, updatedService);
        serviceRepository.save(service);
    }

    /**
     * Atualiza os atributos de um serviço com base nos dados do DTO fornecido.
     *
     * @param service Serviço a ser atualizado.
     * @param updatedService Dados atualizados do serviço.
     */
    private void updateServiceAttributes(Service service, ServiceDTO updatedService) {
        service.setName(updatedService.name());
        service.setDescription(updatedService.description());
        service.setDuration(updatedService.duration());
        service.setPriceInCents(updatedService.priceInCents());
    }

    /**
     * Exclui um serviço específico de um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço a ser excluído.
     */
    public void deleteService(String barberId, String id) {
        Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
        if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
        Service service = optionalService.get();
        serviceRepository.delete(service);
    }

    /**
     * Busca um serviço específico de um barbeiro pelo seu ID.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço.
     * @return Serviço encontrado ou vazio se não existir.
     */
    public Optional<Service> findByBarberIdAndId(String barberId, String id) {
        return serviceRepository.findByBarberIdAndId(barberId, id);
    }

    /**
     * Recupera todos os serviços ativos de um barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @return Lista de serviços ativos do barbeiro.
     */
    public List<ServiceDTO> getActives(String barberId) {
        return new ArrayList<>(serviceRepository.findAllByBarberIdAndActiveTrue(barberId));
    }

    /**
     * Atualiza o status de ativação de um serviço (ativa ou desativa).
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço a ser atualizado.
     */
    public void updateServiceStatus(String barberId, String id) {
        Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
        if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
        Service service = optionalService.get();
        service.setActive(!service.isActive());
    }

    /**
     * Verifica se um serviço existe para um barbeiro específico.
     *
     * @param barberId ID do barbeiro.
     * @param id ID do serviço.
     * @return Verdadeiro se o serviço existir, falso caso contrário.
     */
    public boolean existsByBarberIdAndId(String barberId, String id) {
        return serviceRepository.existsByBarberIdAndId(barberId, id);
    }

    /**
     * Recupera a duração de um serviço para um barbeiro específico.
     *
     * @param barberId ID do barbeiro.
     * @param serviceId ID do serviço.
     * @return Duração do serviço em minutos.
     */
    public int getDurationById(String barberId, String serviceId) {
        return serviceRepository.getDurationByBarberIdAndId(barberId, serviceId);
    }
}
