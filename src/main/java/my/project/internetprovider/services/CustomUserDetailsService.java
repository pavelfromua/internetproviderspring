package my.project.internetprovider.services;

import my.project.internetprovider.models.ProviderUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        ProviderUser providerUser = userService.getUserByLogin(userName);
        if (providerUser == null) {
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }

        UserDetails user = User.builder()
                .username(providerUser.getLogin())
                .password(providerUser.getPassword())
                .roles(userService.getListOfRolesForUser(userName).stream().toArray(String[]::new))
                .build();
        return user;
    }
}
