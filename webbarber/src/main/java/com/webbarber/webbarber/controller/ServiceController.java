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

@RestController
public class ServiceController {
    private final ServiceService serviceService;
    private final BarberService barberService;

    public ServiceController(ServiceService serviceService, BarberService barberService) {
        this.serviceService = serviceService;
        this.barberService = barberService;
    }

    @PostMapping("/barber/services/new")
    @Transactional
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> createService(Authentication authentication, @RequestBody ServiceDTO service) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.createService(barberId, service);
        return ResponseEntity.ok("Serviço criado com sucesso");
    }

    @PutMapping("/barber/services/{serviceId}/update")
    @Transactional
    public ResponseEntity<String> updateService(Authentication authentication, @PathVariable String serviceId, @RequestBody ServiceDTO updatedService) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.updateService(barberId, serviceId, updatedService);
        return ResponseEntity.ok("Serviço atualizado com sucesso");
    }

    @DeleteMapping("/barber/services/{id}/delete")
    @Transactional
    public ResponseEntity<String> deleteService(Authentication authentication, @PathVariable String id) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.deleteService(barberId, id);
        return ResponseEntity.ok("Serviço deletado com sucesso");
    }

    @GetMapping("/services/all/{barberId}")
    public ResponseEntity<List<ServiceDTO>> getAllActiveServices(@PathVariable String barberId) {
        return ResponseEntity.ok(serviceService.getActives(barberId));
    }

    @PutMapping("/barber/services/{id}/status")
    @Transactional
    public ResponseEntity<String> setStatusService(Authentication authentication, @PathVariable String id) {
        String barberId = barberService.findIdByPhone(authentication.getName());
        serviceService.updateServiceStatus(barberId, id);
        return ResponseEntity.ok("Estado do serviço atualizado com sucesso.");

    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
