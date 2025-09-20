package thm.gromokoso.usermanagement.service;

import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.entity.Group;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.model.UserWithGroupRole;

import java.util.List;

@Service
public interface GroupService {

    /**
     * Saves the given Group data within the user management.
     * @param group Object representation of the data which shall be saved.
     * @return User object if successful, null if failed.
     */
    Group saveGroup(Group group);

    /**
     * Returns all saved Group data within the user management.
     * @return List of all saved Groups.
     */
    List<Group> fetchGroupList();

    /**
     * Returns the saved Group data of the Group with the given name.
     * @param name Unique identifier of the group data.
     * @return Group object if successful, null if no group with this name is saved.
     */
    Group getGroupByGroupName(String name);

    /**
     * Updates the group data of the group identified by the given name with the data given in the group parameter.
     * @param group Object representation of the data which shall be saved.
     * @param name Unique identifier of the group data.
     * @return Group object if successful, null if failed.
     */
    Group updateGroupByGroupName(Group group, String name);

    /**
     * Deletes the saved Group data of the Group with the given name.
     * @param name Unique identifier of the group data.
     */
    void deleteGroupByGroupName(String name);

    /**
     * Returns a list of the Users of the Group identified by the given name with their corresponding roles within the group.
     * @param name Unique identifier of the group data.
     * @return List of Users with their corresponding Role.
     */
    List<UserWithGroupRole> fetchUserListFromGroup(String name);

    /**
     * Adds a User to a group so that he will be a members and has access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param user object representation of the user which should be added.
     */
    User addUserToGroupList(String name, User user);

    /**
     * Deletes a User from a group so that he is no longer a members and no longer has access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param username Unique identifier of the user data.
     */
    void deleteUserFromGroup(String name, String username);

    /**
     * Saves the given API ID to the Group data which grants the Group and it's members access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param apiId Unique identifier of an API.
     * @return Api ID if successful, null if failed.
     */
    Integer addApiIdToGroup(String name, Integer apiId);

    /**
     * Returns a list of all API IDs of which grants the group and it's members access to the corresponding tools.
     * @param name Unique identifier of the group data.
     * @return List of API IDs if successful, null if no user with this ID is saved.
     */
    List<Integer> fetchApiIdListFromGroup(String name);

    /**
     * Deletes an API ID from a group so that the group and it's members no longer have access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param apiId Unique identifier of an API.
     */
    void deleteApiIdFromGroup(String name, Integer apiId);
}
