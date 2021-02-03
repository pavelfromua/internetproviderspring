package my.project.internetprovider.repositories;

import my.project.internetprovider.models.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ProviderService, Long> {
}
