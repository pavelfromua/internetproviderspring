package my.project.internetprovider.repositories;

import my.project.internetprovider.models.Product;
import my.project.internetprovider.models.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("SELECT r FROM Plan r WHERE r.product = ?1")
    List<Plan> findAllByProductId(Product product);

    Page<Plan> findAll(Pageable pageable);
}
