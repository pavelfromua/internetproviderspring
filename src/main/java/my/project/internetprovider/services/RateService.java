package my.project.internetprovider.services;

import my.project.internetprovider.models.ProviderService;
import my.project.internetprovider.models.Rate;
import my.project.internetprovider.repositories.ProviderServiceRepository;
import my.project.internetprovider.repositories.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RateService {
    private final RateRepository rateRepository;
    private final ProviderServiceRepository serviceRepository;

    @Autowired
    public RateService(RateRepository rateRepository,
                       ProviderServiceRepository serviceRepository) {
        this.rateRepository = rateRepository;
        this.serviceRepository = serviceRepository;
    }

    public void addNewRate(Rate rate) {
        rateRepository.save(rate);
    }

    public List<Rate> getRates() {
        return rateRepository.findAll();
    }

    public Rate getRateById(Long id) {
        Optional<Rate> optionalRate = rateRepository.findById(id);
        if (!optionalRate.isPresent())
            return null;

        return optionalRate.get();
    }

    @Transactional
    public void updateRate(Rate rate, Long id) {
        Rate rateFromDb = rateRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "rate with id " + id + " does not exists"));

        rateFromDb.setName(rate.getName());
        rateFromDb.setService(rate.getService());
        rateFromDb.setPrice(rate.getPrice());

        rateRepository.save(rateFromDb);
    }

    public void deleteRate(Long id) {
        boolean isExists = rateRepository.existsById(id);

        if (!isExists)
            throw new IllegalStateException("rate with id " + id
                    + " does not exists");

        rateRepository.deleteById(id);
    }

    public List<Rate> getRatesByServiceId(Long serviceId) {
        return rateRepository.findAllByServiceId(serviceRepository.getOne(serviceId));
    }
}
