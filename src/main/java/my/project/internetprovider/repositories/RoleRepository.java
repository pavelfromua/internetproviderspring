package my.project.internetprovider.repositories;

import my.project.internetprovider.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
