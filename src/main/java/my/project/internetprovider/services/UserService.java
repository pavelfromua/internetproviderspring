package my.project.internetprovider.services;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.Plan;
import my.project.internetprovider.models.Product;
import my.project.internetprovider.models.User;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.models.dto.AccountProductPlanDto;
import my.project.internetprovider.repositories.PaymentRepository;
import my.project.internetprovider.repositories.ProductRepository;
import my.project.internetprovider.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       ProductRepository productRepository,
                       PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Unknown user: "+username);

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);

        return userFromDb.orElse(new User());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
            //log here
        }

        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        user.setAccount(Account.newBuilder().setActive(true).build());

        userRepository.save(user);

        return true;
    }

    @Transactional
    public boolean updateUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB == null)
            return false;
            //log here            "user with id " + id + " does not exists"));

        userFromDB.setName(user.getName());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setPassword(user.getPassword());

        userRepository.save(userFromDB);

        return true;
    }

    public boolean deleteUser(Long id) {
        boolean isExists = userRepository.existsById(id);

        if (!isExists)
            return false;
            //throw new IllegalStateException("user with id " + id
            //        + " does not exists");

        userRepository.deleteById(id);

        return true;
    }

    public String getRoleForUser(User user) {
        if (user == null)
            return "";

        Optional<Role> role = user.getRoles().stream().findFirst();
        if (!role.isPresent())
            return "";

        return role.get().getName();
    }

    @Transactional
    public Map<String, ?> getDataForUserCabinet(Long userId) {
        Map<String, Object> modelAttributes = new HashMap<>();

        User user = findUserById(userId);
        List<Product> products = productRepository.findAll();
        Set<Plan> plans = user.getAccount().getPlans();
        AccountProductPlanDto accProductPlanDto;

        List<AccountProductPlanDto> listProductPlan = new ArrayList<>();
        for (Product product: products) {
            accProductPlanDto = new AccountProductPlanDto();
            accProductPlanDto.setProduct(product);
            accProductPlanDto.setPlan(plans.stream().filter(plan -> plan.getProduct() == product)
            .findFirst().orElse(null));
            accProductPlanDto.setDescription(product + ": "
                    + (accProductPlanDto.getPlan() == null ? " not assigned" : accProductPlanDto.getPlan()));

            listProductPlan.add(accProductPlanDto);
        }

        modelAttributes.put("user", user);
        modelAttributes.put("products", listProductPlan);
        modelAttributes.put("balance", paymentRepository.getBalanceByAccount(user.getAccount()));

        return modelAttributes;
    }
}
