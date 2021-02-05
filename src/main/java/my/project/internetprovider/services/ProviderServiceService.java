package my.project.internetprovider.services;

import my.project.internetprovider.models.ProviderService;
import my.project.internetprovider.models.ProviderUser;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.repositories.ProviderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceService {
    private final ProviderServiceRepository serviceRepository;

    @Autowired
    public ProviderServiceService(ProviderServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void addNewService(ProviderService service) {
        serviceRepository.save(service);
    }

    public List<ProviderService> getServices() {
        return serviceRepository.findAll();
    }

    public ProviderService getServiceById(Long id) {
        Optional<ProviderService> optionalService = serviceRepository.findById(id);
        if (!optionalService.isPresent())
            return null;

        return optionalService.get();
    }

    @Transactional
    public void updateService(ProviderService service, Long id) {
        ProviderService providerService = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "service with id " + id + " does not exists"));

        providerService.setName(service.getName());

        serviceRepository.save(providerService);
    }

    public void deleteService(Long id) {
        boolean isExists = serviceRepository.existsById(id);

        if (!isExists)
            throw new IllegalStateException("service with id " + id
                    + " does not exists");

        serviceRepository.deleteById(id);
    }
}
