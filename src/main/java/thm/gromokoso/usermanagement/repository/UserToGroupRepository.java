package thm.gromokoso.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.UserToGroup;
import thm.gromokoso.usermanagement.entity.UserToGroupId;

import java.util.List;

public interface UserToGroupRepository extends JpaRepository<UserToGroup, UserToGroupId> {
    List<UserToGroup> findByUser_UserName(String userName);
    List<UserToGroup> findByGroup_GroupName(String groupName);
    void deleteByUserUserNameAndGroupGroupName(String userName, String groupName);
    @Query("SELECT COUNT(u) FROM UserToGroup u WHERE u.id.groupName = :groupName AND u.groupRole = :groupRole")
    Long countUserByGroupRole(@Param("groupName") String groupName, @Param("groupRole") EGroupRole groupRole);
}

