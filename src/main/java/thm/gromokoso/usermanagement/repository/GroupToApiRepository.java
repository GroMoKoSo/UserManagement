package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thm.gromokoso.usermanagement.entity.GroupToApi;
import thm.gromokoso.usermanagement.entity.GroupToApiId;

public interface GroupToApiRepository extends JpaRepository<GroupToApi, GroupToApiId> { }
