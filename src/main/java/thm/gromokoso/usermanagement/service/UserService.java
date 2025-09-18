package thm.gromokoso.usermanagement.service;

import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.model.GroupWithGroupRole;

import java.util.List;

@Service
public interface UserService {

    /**
     * Saves the given User data within the user management.
     * @param user Object representation of the data which shall be saved.
     * @return User object if successful, null if failed.
     */
    User saveUser(User user);

    /**
     * Returns all saved User data within the user management.
     * @return List of all saved Users.
     */
    List<User> fetchUserList();

    /**
     * Returns the saved User data of the User with the given ID.
     * @param userName Unique identifier of the user data.
     * @return User object if successful, null if no user with this ID is saved.
     */
    User findUserByUserName(String userName);

    /**
     * Deletes the saved User data of the User with the given ID.
     * @param userName Unique identifier of the user data.
     */
    void deleteUserByUserName(String userName);

    /**
     * Saves the given API ID to the User data which grants the User access to the corresponding tool.
     * @param userName Unique identifier of the user data.
     * @param api_id Unique identifier of an API.
     */
    void addApiToUser(String userName, int api_id);

    /**
     * Returns a list of all API IDs of which grants the user access to the corresponding tools.
     * @param userName Unique identifier of the user data.
     * @return List of API IDs if successful, null if no user with this ID is saved.
     */
    List<Integer> fetchApiListFromUser(String userName);

    /**
     * Deletes an API ID from a user so that the user no longer has access to the corresponding tool.
     * @param userName Unique identifier of the user data.
     * @param api_id Unique identifier of an API.
     */
    void deleteApiIdFromUser(String userName, int api_id);

    /**
     * Returns a list of groups which the user is part of and also his corresponding role within the groups.
     * @param userName Unique identifier of the user data.
     * @return List of groups with the corresponding user role.
     */
    List<GroupWithGroupRole> fetchGroupListFromUser(String userName);
}
