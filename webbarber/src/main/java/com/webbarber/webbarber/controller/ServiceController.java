package com.webbarber.webbarber.controller;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.service.ServiceService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("/new")
    @Transactional
    public ResponseEntity<Void> createService(@RequestBody ServiceDTO service) {
        serviceService.createService(service);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update")
    @Transactional
    public ResponseEntity<Void> updateService(@RequestBody ServiceDTO updatedService, @PathVariable Long id) {
        serviceService.updateService(id, updatedService);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    @Transactional
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}
