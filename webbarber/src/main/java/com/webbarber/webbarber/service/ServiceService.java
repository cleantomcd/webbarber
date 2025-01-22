package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.ServiceDTO;
import com.webbarber.webbarber.entity.Service;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
  private final ServiceRepository serviceRepository;

  public ServiceService(ServiceRepository serviceRepository) {
      this.serviceRepository = serviceRepository;
  }

  public void createService(String barberId, ServiceDTO service) {
      Service newService = new Service(barberId, service);
      serviceRepository.save(newService);
  }

  public void updateService(String barberId, String id, ServiceDTO updatedService) {
      Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
      if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
      Service service = optionalService.get();
      updateServiceAttributes(service, updatedService);
      serviceRepository.save(service);
  }

  private void updateServiceAttributes(Service service, ServiceDTO updatedService) {
      service.setName(updatedService.name());
      service.setDescription(updatedService.description());
      service.setDuration(updatedService.duration());
      service.setPriceInCents(updatedService.priceInCents());
  }

  public void deleteService(String barberId, String id) {
      Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
      if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
      Service service = optionalService.get();
      serviceRepository.delete(service);
  }

  public Optional<Service> findByBarberIdAndId(String barberId, String id) {
      return serviceRepository.findByBarberIdAndId(barberId, id);
  }

  public List<ServiceDTO> getActives(String barberId) {
      return new ArrayList<>(serviceRepository.findAllByBarberIdAndActiveTrue(barberId));
  }

  public void updateServiceStatus(String barberId, String id) {
      Optional<Service> optionalService = findByBarberIdAndId(barberId, id);
      if(optionalService.isEmpty()) throw new ServiceNotFoundException("Serviço não encontrado");
      Service service = optionalService.get();
      service.setActive(!service.isActive());
  }

  public boolean existsByBarberIdAndId(String barberId, String id) {
      return serviceRepository.existsByBarberIdAndId(barberId, id);
  }

  public int getDurationById(String barberId, String serviceId) {
      return serviceRepository.getDurationByBarberIdAndId(barberId, serviceId);
  }
}
