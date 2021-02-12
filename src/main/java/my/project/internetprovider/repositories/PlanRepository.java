package my.project.internetprovider.repositories;

import my.project.internetprovider.models.Product;
import my.project.internetprovider.models.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanRepository extends PagingAndSortingRepository<Plan, Long> {
    @Query("SELECT p FROM Plan p WHERE p.product = ?1")
    List<Plan> findAllByProductId(Product product);

    @Query("SELECT p FROM Plan p ORDER BY p.product.name")
    List<Plan> findAll();
}
