package my.project.internetprovider.repositories;

import my.project.internetprovider.models.ProviderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ProviderUser, Long> {
    @Query("SELECT u FROM ProviderUser u WHERE u.email = ?1")
    Optional<ProviderUser> findByEmail(String email);

    @Query("SELECT u FROM ProviderUser u WHERE u.login = ?1")
    Optional<ProviderUser> findByLogin(String name);
}
