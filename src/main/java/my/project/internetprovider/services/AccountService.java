package my.project.internetprovider.services;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.FlowDirection;
import my.project.internetprovider.models.Payment;
import my.project.internetprovider.models.Plan;
import my.project.internetprovider.repositories.AccountRepository;
import my.project.internetprovider.repositories.PaymentRepository;
import my.project.internetprovider.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final PlanService planService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          PlanService planService,
                          PaymentRepository paymentRepository,
                          PlanRepository planRepository) {
        this.accountRepository = accountRepository;
        this.planService = planService;
        this.planRepository = planRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Plan> getPlansByProductId(Long productId) {
        return planService.getPlansByProductId(productId);
    }

    public void setAccountStatusByBalance(Account account) {
        if (account.isActive() && getBalanceByAccount(account) <= 0) {
            account.setActive(false);
            accountRepository.save(account);
        }

        if (!account.isActive() && getBalanceByAccount(account) > 0)
            account.setActive(true);
        accountRepository.save(account);
    }

    @Transactional
    public void assignPlanToAccount(Long accountId, Long planId) {
        Account account = accountRepository.getOne(accountId);

        Set<Plan> plans = account.getPlans();

        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            Plan plan = optionalPlan.get();
            plans.removeIf(p -> p.getProduct().getId() == plan.getProduct().getId());
            plans.add(plan);
            account.setPlans(plans);
            accountRepository.save(account);

            paymentRepository.save(Payment.newBuilder()
                    .setAccount(account)
                    .setFd(FlowDirection.IN)
                    .setAmount(-plan.getPrice())
                    .setName("Fee is charged for " + plan.getProduct() + ": " + plan)
                    .setDate(LocalDateTime.now())
                    .build());

            setAccountStatusByBalance(account);
        }
    }

    @Transactional
    public void savePayment(Long accountId, Double amount) {
        Account account = accountRepository.getOne(accountId);

        paymentRepository.save(Payment.newBuilder()
                .setAccount(account)
                .setFd(FlowDirection.IN)
                .setAmount(amount)
                .setName("Subscription fee")
                .setDate(LocalDateTime.now())
                .build());

        setAccountStatusByBalance(account);
    }

    public Double getBalanceByAccount(Account account) {
        return paymentRepository.getBalanceByAccount(account);
    }

    @Transactional
    public void setAccountStatus(Long accountId, Boolean status) {
        Account account = accountRepository.getOne(accountId);
        account.setActive(status);

        accountRepository.save(account);
    }
}
