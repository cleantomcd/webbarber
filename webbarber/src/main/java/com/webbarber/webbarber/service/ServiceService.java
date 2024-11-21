package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.entity.Service;
import com.webbarber.webbarber.repository.ServiceRepository;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
  private final ServiceRepository serviceRepository;

  public ServiceService(ServiceRepository serviceRepository) {
      this.serviceRepository = serviceRepository;
  }

  public void createService(ServiceDTO service) {
      Service newService = new Service(service);
      serviceRepository.save(newService);
  }

  public void updateService(String id, ServiceDTO updatedService) {
      Optional<Service> optionalService = findById(id);
      if(optionalService.isEmpty()) throw new IllegalArgumentException();
      Service service = optionalService.get();
      updateServiceAttributes(service, updatedService);
      serviceRepository.save(service);
  }

  private void updateServiceAttributes(Service service, ServiceDTO updatedService) {
      service.setName(updatedService.name());
      service.setDescription(updatedService.description());
      service.setEstimatedTime(updatedService.estimatedTime());
      service.setPriceInCents(updatedService.priceInCents());
  }

  public void deleteService(String id) {
      Optional<Service> optionalService = findById(id);
      if(optionalService.isEmpty()) throw new IllegalArgumentException();
      Service service = optionalService.get();
      serviceRepository.delete(service);
  }

  public Optional<Service> findById(String id) {
      return serviceRepository.findById(id);
  }
}
