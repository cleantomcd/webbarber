package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.service.BarberService;
import com.webbarber.webbarber.service.ServiceService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável pelo gerenciamento de serviços oferecidos pelos barbeiros.
 * Permite a criação, atualização, exclusão e listagem de serviços, além da alteração de status.
 */
@RestController
public class ServiceController {
    private final ServiceService serviceService;
    private final BarberService barberService;

    /**
     * Construtor da classe {@code ServiceController}.
     *
     * @param serviceService Serviço responsável pelo gerenciamento de serviços de barbearia.
     * @param barberService  Serviço responsável pelo gerenciamento de barbeiros.
     */
    public ServiceController(ServiceService serviceService, BarberService barberService) {
        this.serviceService = serviceService;
        this.barberService = barberService;
    }

    /**
     * Cria um novo serviço para um barbeiro autenticado.
     *
     * @param authentication Informações do usuário autenticado.
     * @param service        Dados do serviço a ser criado.
     * @return Resposta HTTP indicando sucesso ou falha na criação.
     */
    @PostMapping("/barber/services/new")
    @Transactional
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> createService(Authentication authentication, @RequestBody ServiceDTO service) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.createService(barberId, service);
        return ResponseEntity.ok("Serviço criado com sucesso");
    }

    /**
     * Atualiza um serviço existente de um barbeiro autenticado.
     *
     * @param authentication  Informações do usuário autenticado.
     * @param serviceId       Identificador do serviço a ser atualizado.
     * @param updatedService  Dados atualizados do serviço.
     * @return Resposta HTTP indicando sucesso ou falha na atualização.
     */
    @PutMapping("/barber/services/{serviceId}/update")
    @Transactional
    public ResponseEntity<String> updateService(Authentication authentication, @PathVariable String serviceId, @RequestBody ServiceDTO updatedService) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.updateService(barberId, serviceId, updatedService);
        return ResponseEntity.ok("Serviço atualizado com sucesso");
    }

    /**
     * Exclui um serviço de um barbeiro autenticado.
     *
     * @param authentication Informações do usuário autenticado.
     * @param id            Identificador do serviço a ser deletado.
     * @return Resposta HTTP indicando sucesso ou falha na exclusão.
     */
    @DeleteMapping("/barber/services/{id}/delete")
    @Transactional
    public ResponseEntity<String> deleteService(Authentication authentication, @PathVariable String id) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.deleteService(barberId, id);
        return ResponseEntity.ok("Serviço deletado com sucesso");
    }

    /**
     * Retorna todos os serviços ativos de um barbeiro.
     *
     * @param barberId Identificador do barbeiro cujos serviços ativos serão retornados.
     * @return Lista de serviços ativos do barbeiro.
     */
    @GetMapping("/services/all/{barberId}")
    public ResponseEntity<List<ServiceDTO>> getAllActiveServices(@PathVariable String barberId) {
        return ResponseEntity.ok(serviceService.getActives(barberId));
    }

    /**
     * Atualiza o status de um serviço específico de um barbeiro autenticado.
     *
     * @param authentication Informações do usuário autenticado.
     * @param id            Identificador do serviço cujo status será atualizado.
     * @return Resposta HTTP indicando sucesso na atualização do status.
     */
    @PutMapping("/barber/services/{id}/status")
    @Transactional
    public ResponseEntity<String> setStatusService(Authentication authentication, @PathVariable String id) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.updateServiceStatus(barberId, id);
        return ResponseEntity.ok("Estado do serviço atualizado com sucesso.");
    }

    /**
     * Manipula a exceção {@code ServiceNotFoundException}, retornando um status HTTP 404.
     *
     * @param ex Exceção capturada.
     * @return Resposta HTTP com a mensagem da exceção.
     */
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
