package my.project.internetprovider.repositories;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.Payment;
import my.project.internetprovider.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT sum(p.amount) FROM Payment p WHERE p.account = ?1")
    Double getBalanceByAccount(Account account);
}
