package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thm.gromokoso.usermanagement.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
}
