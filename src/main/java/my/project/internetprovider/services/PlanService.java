package my.project.internetprovider.services;

import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.models.Plan;
import my.project.internetprovider.models.Product;
import my.project.internetprovider.repositories.ProductRepository;
import my.project.internetprovider.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PlanService(PlanRepository planRepository,
                       ProductRepository productRepository) {
        this.planRepository = planRepository;
        this.productRepository = productRepository;
    }

    public void savePlan(Plan plan) throws DBException {
        try {
            planRepository.save(plan);
        } catch (Exception e) {
            throw new DBException("plan with id " + plan.getId()
                    + " can't be saved into db", e);
        }

    }

    public Page<Plan> getPlans(int pageNumber, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);

        return planRepository.findAll(pageable);
    }

    public List<Plan> getPlans() {
        return planRepository.findAll();
    }

    public Plan getPlanById(Long id) {
        Optional<Plan> optionalPlan = planRepository.findById(id);
        if (!optionalPlan.isPresent())
            throw new IllegalStateException("plan with id " + id + " does not exists");

        return optionalPlan.get();
    }

    @Transactional
    public void updatePlan(Plan plan) {
        Plan planFromDb = planRepository.findById(plan.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "plan with id " + plan.getId() + " does not exists"));

        planFromDb.setName(plan.getName());
        planFromDb.setProduct(plan.getProduct());
        planFromDb.setPrice(plan.getPrice());

        planRepository.save(planFromDb);
    }

    public void deletePlan(Long id) throws DBException {
        boolean isExists = planRepository.existsById(id);

        if (!isExists)
            throw new IllegalStateException("plan with id " + id
                    + " does not exists");

       try {
            planRepository.deleteById(id);
       } catch (Exception e) {
            throw new DBException("plan with id " + id
                    + " can't be deleted because of using in other entities", e);
       }
    }

    @Transactional
    public List<Plan> getPlansByProductId(Long productId) {
        return planRepository.findAllByProductId(productRepository.getOne(productId));
    }

    @Transactional
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Map<String, ?> getDataForListOfPlansByProductId(Long productId) {
        Map<String, Object> modelAttributes = new HashMap<>();

        modelAttributes.put("products", getProducts());
        modelAttributes.put("plans", getPlansByProductId(productId));

        return modelAttributes;
    }

    @Transactional
    public Map<String, ?> getDataForEditPlan(Long id) {
        Map<String, Object> modelAttributes = new HashMap<>();

        modelAttributes.put("products", getProducts());
        modelAttributes.put("plan", getPlanById(id));

        return modelAttributes;
    }
}
