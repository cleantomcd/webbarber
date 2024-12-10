package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.service.ServiceService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("/new")
    @Transactional
    public ResponseEntity<String> createService(@RequestBody ServiceDTO service) {
        serviceService.createService(service);
        return ResponseEntity.ok("Serviço criado com sucesso");
    }

    @PutMapping("/{id}/update")
    @Transactional
    public ResponseEntity<String> updateService(@RequestBody ServiceDTO updatedService, @PathVariable String id) {
        serviceService.updateService(id, updatedService);
        return ResponseEntity.ok("Serviço atualizado com sucesso");
    }

    @DeleteMapping("/{id}/delete")
    @Transactional
    public ResponseEntity<String> deleteService(@PathVariable String id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok("Serviço deletado com sucesso");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceDTO>> getAllActiveServices() {
        return ResponseEntity.ok(serviceService.getActives());
    }

    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<String> setStatusService(@PathVariable String id) {
        serviceService.updateServiceStatus(id);
        return ResponseEntity.ok("Estado do serviço atualizado com sucesso.");

    }
}
