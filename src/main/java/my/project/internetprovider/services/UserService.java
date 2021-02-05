package my.project.internetprovider.services;

import my.project.internetprovider.models.Account;
import my.project.internetprovider.models.ProviderUser;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ProviderUser> getUsers() {
       return userRepository.findAll();
    }

    public ProviderUser getUserById(Long id) {
        Optional<ProviderUser> optionalMyUser = userRepository.findById(id);
        if (!optionalMyUser.isPresent())
            return null;

        return optionalMyUser.get();
    }

    public void addNewUser(ProviderUser user, Role role) {
        Optional<ProviderUser> userOptional = userRepository.findByLogin(user.getLogin());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("login taken");
        }

        ProviderUser providerUser = ProviderUser.newBuilder()
                .setLogin(user.getLogin())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActive(true)
                .setRoles(Collections.singleton(role))
                .setAccount(Account.newBuilder().setActive(true).build())
                .build();

        userRepository.save(providerUser);
    }

    public ProviderUser getUserByLogin(String login) {
        Optional<ProviderUser> optionalMyUser = userRepository.findByLogin(login);
        if (!optionalMyUser.isPresent())
            return null;

        return optionalMyUser.get();
    }

    public List<String> getListOfRolesForUser(String login) {
        ProviderUser providerUser = getUserByLogin(login);
        if (providerUser == null)
            return new ArrayList<>();

        List<String> roles = providerUser.getRoles().stream().map(role -> role.name()).collect(Collectors.toList());

        return roles;
    }

    public String getCurrentRole(String login) {
        List<String> roles = getListOfRolesForUser(login);

        if (roles.size() == 0)
            return "";

        return roles.get(0);
    }

    @Transactional
    public void updateUser(ProviderUser providerUser, Long id) {
        ProviderUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + id + " does not exists"));

        user.setName(providerUser.getName());
        user.setEmail(providerUser.getEmail());
        user.setPassword(providerUser.getPassword());

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        boolean isExists = userRepository.existsById(id);

        if (!isExists)
            throw new IllegalStateException("user with id " + id
                    + " does not exists");

        userRepository.deleteById(id);
    }
}
