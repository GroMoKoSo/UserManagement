package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thm.gromokoso.usermanagement.entity.Group;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.apiAccesses WHERE g.groupName = :groupName")
    Optional<Group> findByIdWithApis(@Param("groupName") String groupName);
}
