package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thm.gromokoso.usermanagement.entity.UserToGroup;
import thm.gromokoso.usermanagement.entity.UserToGroupId;

import java.util.List;

public interface UserToGroupRepository extends JpaRepository<UserToGroup, UserToGroupId> {
    List<UserToGroup> findByUser_UserName(String userName);
    List<UserToGroup> findByGroup_GroupName(String groupName);
    void deleteByUserUserNameAndGroupGroupName(String userName, String groupName);
}

