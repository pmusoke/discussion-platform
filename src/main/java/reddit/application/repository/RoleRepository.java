package reddit.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reddit.application.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
