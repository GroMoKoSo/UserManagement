package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thm.gromokoso.usermanagement.entity.UserToApi;
import thm.gromokoso.usermanagement.entity.UserToApiId;

public interface UserToApiRepository extends JpaRepository<UserToApi, UserToApiId> { }
