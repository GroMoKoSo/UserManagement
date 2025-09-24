package thm.gromokoso.usermanagement.service;

import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.dto.*;

import java.util.List;

@Service
public interface UserService {

    /**
     * Saves the given User data within the user management.
     * @param user Object representation of the data which shall be saved.
     * @return User object if successful, null if failed.
     */
    UserDto saveUser(UserDto user);

    /**
     * Returns all saved User data within the user management.
     * @return List of all saved Users.
     */
    List<UserDto> fetchUserList();

    /**
     * Returns the saved User data of the User with the given username.
     * @param username Unique identifier of the user data.
     * @return User object if successful, null if no user with this username is saved.
     */
    UserDto findUserByUserName(String username);

    /**
     * Updates the user data of the user identified by the given username with the data given in the user parameter.
     * @param username Unique identifier of the user data.
     * @param user Object representation of the data which shall be saved.
     * @return User object if successful, null if failed.
     */
    UserDto updateUser( String username, UpdateUserDto user);

    /**
     * Deletes the saved User data of the User with the given ID.
     * @param username Unique identifier of the user data.
     */
    void deleteUserByUserName(String username);

    /**
     * Saves the given API ID to the User data which grants the User access to the corresponding tool.
     * @param username Unique identifier of the user data.
     * @param userToApiIdDto Dataset of the API which should be updated from the User including ID and whether its active.
     * @return Api ID if successful, null if failed.
     */
    UserToApiDto addApiToUser(String username, UserToApiDto userToApiIdDto);

    /**
     * Returns a list of all API IDs of which grants the user access to the corresponding tools.
     * @param username Unique identifier of the user data.
     * @param accessViaGroup Whether the request should also return API ID's for which the user has access because of the membership of a group.
     * @return List of API IDs and whether their active if successful, null if no user with this ID is saved.
     */
    List<UserToApiDto> fetchApiListFromUser(String username, boolean accessViaGroup);

    /**
     * Deletes an API ID from a user so that the user no longer has access to the corresponding tool.
     * @param username Unique identifier of the user data.
     * @param apiId Unique identifier of an API.
     * @param userToApiIdDto Dataset of the API which should be updated from the User including ID and whether its active.
     */
    UserToApiDto updateApiFromUser(String username, Integer apiId, UpdateUserToApiDto userToApiIdDto);

    /**
     * Deletes an API ID from a user so that the user no longer has access to the corresponding tool.
     * @param username Unique identifier of the user data.
     * @param apiId Unique identifier of an API.
     */
    void deleteApiIdFromUser(String username, Integer apiId);

    /**
     * Returns a list of groups which the user is part of and also his corresponding role within the groups.
     * @param username Unique identifier of the user data.
     * @return List of groups with the corresponding user role.
     */
    List<GroupWithGroupRoleDto> fetchGroupListFromUser(String username);
}
