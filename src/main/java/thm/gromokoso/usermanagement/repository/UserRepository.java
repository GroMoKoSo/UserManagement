package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thm.gromokoso.usermanagement.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.apiAccesses WHERE u.userName = :username")
    Optional<User> findByIdWithApis(@Param("username") String username);
}
