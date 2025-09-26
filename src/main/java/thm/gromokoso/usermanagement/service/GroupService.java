package thm.gromokoso.usermanagement.service;

import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.dto.*;

import java.util.List;

@Service
public interface GroupService {

    /**
     * Saves the given Group data within the user management.
     * @param group Object representation of the data which shall be saved.
     * @return User object if successful, null if failed.
     */
    GroupDto saveGroup(GroupDto group);

    /**
     * Returns all saved Group data within the user management.
     * @param privateVisibility Whether groups with the attribute visibility = private should be fetched.
     * @return List of all saved Groups.
     */
    List<GroupDto> fetchGroupList(boolean privateVisibility);

    /**
     * Returns the saved Group data of the Group with the given name.
     * @param name Unique identifier of the group data.
     * @return Group object if successful, null if no group with this name is saved.
     */
    GroupDto getGroupByGroupName(String name);

    /**
     * Updates the group data of the group identified by the given name with the data given in the group parameter.
     * @param name Unique identifier of the group data.
     * @param group Object representation of the data which shall be saved.
     * @return Group object if successful, null if failed.
     */
    GroupDto updateGroupByGroupName(String name, UpdateGroupDto group);

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
    List<UserWithGroupRoleDto> fetchUserListFromGroup(String name);

    /**
     * Adds a User to a group so that he will be a members and has access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param userWithGroupRoleDto Dataset of a User and an EGroupRole.
     */
    UserWithGroupRoleDto addUserToGroupList(String name, UserWithGroupRoleDto userWithGroupRoleDto);

    /**
     * Updates the Userdata from a user of the group.
     * @param name Unique identifier of the group data.
     * @param username Unique identifier of the user data.
     * @param userWithGroupRoleDto Dataset of a User and an EGroupRole.
     */
    UserWithGroupRoleDto updateUserFromGroup(String name, String username, UpdateUserWithGroupRoleDto userWithGroupRoleDto);

    /**
     * Deletes a User from a group so that he is no longer a members and no longer has access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param username Unique identifier of the user data.
     */
    void deleteUserFromGroup(String name, String username);

    /**
     * Saves the given API ID to the Group data which grants the Group and it's members access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param groupToApiDto Dataset containing the data of the API.
     * @return Api ID if successful, null if failed.
     */
    GroupToApiDto addApiIdToGroup(String name, GroupToApiDto groupToApiDto);

    /**
     * Returns a list of all API IDs of which grants the group and it's members access to the corresponding tools.
     * @param name Unique identifier of the group data.
     * @return List of API IDs if successful, null if no user with this ID is saved.
     */
    List<GroupToApiDto> fetchApiIdListFromGroup(String name);

    /**
     * Updates an API ID from a group so that the group and it's members have access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param apiId Unique identifier of an API.
     * @param groupToApiDto Dataset containing the new Data of the API.
     */
    GroupToApiDto updateApiIdFromGroup(String name, Integer apiId, UpdateGroupToApiDto groupToApiDto);

    /**
     * Deletes an API ID from a group so that the group and it's members no longer have access to the corresponding tool.
     * @param name Unique identifier of the group data.
     * @param apiId Unique identifier of an API.
     */
    void deleteApiIdFromGroup(String name, Integer apiId);
}
