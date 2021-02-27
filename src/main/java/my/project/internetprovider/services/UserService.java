package my.project.internetprovider.services;

import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.Payment;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent())
            throw new UsernameNotFoundException("Unknown user: "+username);

        return userOptional.get();
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);

        return userFromDb.orElse(new User());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) throws DBException {
        try {
            user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
            user.setAccount(Account.newBuilder().setActive(true).build());

            userRepository.save(user);
        } catch (Exception e) {
            throw new DBException("user with login " + user.getUsername() +
                    " can't be saved into db", e);
        }
    }

    @Transactional
    public void updateUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            User userFromDB = optionalUser.get();
            userFromDB.setName(user.getName());
            userFromDB.setEmail(user.getEmail());
            userFromDB.setPassword(user.getPassword());

            userRepository.save(userFromDB);
        }
    }

    @Transactional
    public boolean deleteUser(Long id) {
       if (!userRepository.existsById(id))
            return false;

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
        Account account = user.getAccount();

        List<Product> products = productRepository.findAll();
        Set<Plan> plans = user.getAccount().getPlans();
        AccountProductPlanDto accProductPlanDto;
        Plan planDummy = new Plan();
        planDummy.setName("not assigned");
        planDummy.setId(0L);

        List<AccountProductPlanDto> listProductPlan = new ArrayList<>();
        for (Product product: products) {
            accProductPlanDto = new AccountProductPlanDto();
            accProductPlanDto.setProduct(product);
            accProductPlanDto.setPlan(plans.stream().filter(plan -> plan.getProduct() == product)
            .findFirst().orElse(planDummy));
            accProductPlanDto.setDescription(product + ": " + accProductPlanDto.getPlan());

            listProductPlan.add(accProductPlanDto);
        }

        List sortedPayments = new ArrayList(account.getPayments());
        Collections.sort(sortedPayments, (Comparator<Payment>) (one, other) -> other.getDate().compareTo(one.getDate()));

        modelAttributes.put("user", user);
        modelAttributes.put("payments", sortedPayments);
        modelAttributes.put("products", listProductPlan);
        modelAttributes.put("balance", paymentRepository.getBalanceByAccount(user.getAccount()));

        return modelAttributes;
    }
}
