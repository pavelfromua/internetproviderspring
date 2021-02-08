package my.project.internetprovider.services;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class InstallService {
    private final UserService userService;

    @Autowired
    public InstallService(UserService userService) {
        this.userService = userService;
    }

    private boolean isInstalled() {
        return !userService.getUsers().isEmpty();
    }

    public void loadInitDataIntoDB() {
        if (isInstalled())
            //throw new IllegalStateException("Database initialization has been done");
            return;

        User user = User.newBuilder()
                .setUsername("admin")
                .setName("administrator")
                .setEmail("admin@final.pr")
                .setPassword("111")
                .setRoles(Collections.singleton(new Role(1L, "ROLE_ADMIN")))
                .setAccount(Account.newBuilder().setActive(true).build())
                .build();

        userService.saveUser(user);
    }
}
