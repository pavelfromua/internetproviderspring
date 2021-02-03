package my.project.internetprovider.repositories;

import my.project.internetprovider.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    @Query("SELECT r FROM Rate r WHERE r.service = ?1")
    List<Rate> findAllByServiceId(Long serviceId);
}
