package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thm.gromokoso.usermanagement.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
