package my.project.internetprovider.services;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.ProviderUser;
import my.project.internetprovider.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Account account) {
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        if (!optionalAccount.isPresent())
            return null;

        return optionalAccount.get();
    }


}
