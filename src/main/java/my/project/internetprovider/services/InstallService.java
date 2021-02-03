package my.project.internetprovider.services;

import my.project.internetprovider.models.Role;
import my.project.internetprovider.models.ProviderUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        ProviderUser providerUser = ProviderUser.newBuilder()
                .setLogin("admin")
                .setName("administrator")
                .setEmail("admin@final.pr")
                .setPassword("111")
                .setActive(true)
                .build();

        userService.addNewUser(providerUser, Role.ADMIN);
    }
}
