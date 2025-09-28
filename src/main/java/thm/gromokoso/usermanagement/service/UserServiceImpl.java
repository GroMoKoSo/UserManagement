package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.client.McpManagementClient;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.exception.InvalidNameException;
import thm.gromokoso.usermanagement.repository.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToApiRepository userToApiRepository;
    private final UserToGroupRepository userToGroupRepository;
    private final GroupRepository groupRepository;
    private final McpManagementClient mcpManagementClient;

    Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,
                           UserToApiRepository userToApiRepository,
                           UserToGroupRepository userToGroupRepository,
                           GroupRepository groupRepository,
                           McpManagementClient mcpManagementClient) {
        this.userRepository = userRepository;
        this.userToApiRepository = userToApiRepository;
        this.userToGroupRepository = userToGroupRepository;
        this.groupRepository = groupRepository;
        this.mcpManagementClient = mcpManagementClient;
    }

    @Override
    public UserWithSystemRoleDto saveUser(UserDto userDto) {
        logger.info("====== Starting save name transaction ======");
        logger.debug("Replacing whitespaces of username with '-' characters.");
        User dbUser = new User(userDto.userName().replace(" ", "-"), userDto.firstName(), userDto.lastName(), userDto.email(), new ArrayList<>(), new ArrayList<>(), ESystemRole.MEMBER);

        if (!userDto.userName().matches("^[A-Za-z0-9-]+$")) {
            logger.error("Username contains not allowed characters. Allowed are 'A-Z', 'a-z', '0-9' and '-'. Aborting Transaction...");
            throw new InvalidNameException("Username contains illegal characters");
        }

        logger.debug("Saving user to database.");
        userRepository.save(dbUser);
        logger.info("====== Ending save name transaction: SUCCESS ======");
        return convertUserToUserWithSystemRoleDto(dbUser);
    }

    @Override
    public List<UserWithSystemRoleDto> fetchUserList() {
        logger.info("====== Starting to fetch users ======");
        List<UserWithSystemRoleDto> userWithSystemRoleDtos = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userWithSystemRoleDtos.add(new UserWithSystemRoleDto(user.getUserName(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getSystemRole()));
        }

        logger.info("====== Ending to fetch users: SUCCESS ======");
        return userWithSystemRoleDtos;
    }

    @Override
    public UserWithSystemRoleDto findUserByUserName(String username) {
        logger.info("====== Starting to fetch users by name ======");
        logger.debug("Try to fetch user with name: '{}'", username);
        User dbUser = userRepository.findById(username).orElseThrow();
        logger.info("====== Ending to fetch users by name: SUCCESS ======");
        return convertUserToUserWithSystemRoleDto(dbUser);
    }

    @Override
    @Transactional
    public UserWithSystemRoleDto updateUser(String username,
                                            UpdateUserDto updateUserDto,
                                            boolean canChangeSystemRole) {
        // Get Database References
        logger.info("====== Starting update user transaction ======");
        User dbUser = userRepository.findById(username).orElseThrow();

        // Update Values
        logger.debug("Updating attributes of user: '{}'...", username);
        dbUser.setFirstName(updateUserDto.firstName());
        dbUser.setLastName(updateUserDto.lastName());
        dbUser.setEmail(updateUserDto.email());

        if (canChangeSystemRole) {
            logger.debug("Also updating system role of user: '{}...'", username);
            dbUser.setSystemRole(updateUserDto.systemRole());
        }

        // Save
        logger.debug("Saving updated user to database.");
        userRepository.save(dbUser);
        logger.info("====== Ending update user transaction ======");
        return convertUserToUserWithSystemRoleDto(dbUser);
    }

    @Override
    @Transactional
    public void deleteUserByUserName(String username) {
        logger.info("====== Starting delete user transaction ======");
        logger.debug("Try to fetch user with name: '{}'", username);
        userRepository.findById(username).orElseThrow();
        userRepository.deleteById(username);
        logger.info("====== Ending delete user transaction: SUCCESS ======");
    }

    @Override
    @Transactional
    public UserToApiDto addApiToUser(String username, UserToApiDto userToApiIdDto) {
        // Get Database References
        logger.info("====== Starting add API ID from user transaction ======");
        logger.debug("Try to fetch user with name: '{}'", username);
        User dbUser = userRepository.findById(username).orElseThrow();

        // Create Entity
        UserToApi userToApi = new UserToApi(userToApiIdDto.apiId(), dbUser, userToApiIdDto.active());

        // Save
        logger.debug("Saving new API with ID: '{}' to user with name: '{}'", userToApiIdDto.apiId(), username);
        userToApiRepository.save(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
        logger.info("====== Ending add API ID from user transaction: SUCCESS ======");
        return userToApiIdDto;
    }

    @Override
    @Transactional
    public List<UserToApiDto> fetchApiListFromUser(String username, boolean accessViaGroup) {
        logger.info("====== Starting fetch API IDs from user transaction ======");
        logger.debug("Try to fetch user with name: '{}'", username);
        User dbUser = userRepository.findByIdWithApis(username).orElseThrow();
        logger.debug("Fetch API IDs which the user has access to...");
        List<UserToApiDto> userToApiDtoList = new ArrayList<>();
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        for (UserToApi userToApi : apiAccesses) {
            logger.debug("Found API ID '{}' access from user: '{}' with status '{}'", userToApi.getId().getApiId(),
                    username, userToApi.isActive());
            userToApiDtoList.add(new UserToApiDto(userToApi.getId().getApiId(), "user", userToApi.isActive()));
        }

        // Add API ID's from groups which the user is part of
        if (accessViaGroup) {
            logger.debug("Fetch API IDs which the user has access to because of group membership...");
            List<UserToGroup> userToGroupList = userToGroupRepository.findByUser_UserName(username);
            for (UserToGroup userToGroup : userToGroupList) {
                logger.debug("Try to fetch group with name: '{}'...", userToGroup.getGroup().getGroupName());
                Group dbGroup = groupRepository.findByIdWithApis(userToGroup.getGroup().getGroupName()).orElseThrow();
                for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
                    // Check whether API ID is already in an UserToApiAccess Object in List or if Group Access is active while user access in inactive
                    boolean newApiAccess = userToApiDtoList.stream().noneMatch(
                            userToApiDto -> userToApiDto.apiId().equals(groupToApi.getId().getApiId())
                    );
                    Optional<UserToApiDto> inactiveUserToApiDto = userToApiDtoList.stream().filter(
                            userToApiDto -> userToApiDto.apiId().equals(
                                    groupToApi.getId().getApiId()) && !userToApiDto.active() && groupToApi.isActive()
                    ).findFirst();

                    if (newApiAccess)
                    {
                        logger.debug("Found API ID '{}' access from group: '{}' with status '{}'",
                                groupToApi.getId().getApiId(), dbGroup.getGroupName(), groupToApi.isActive());
                        userToApiDtoList.add(new UserToApiDto(groupToApi.getId().getApiId(),
                                dbGroup.getGroupName(), groupToApi.isActive()));
                    } else if (inactiveUserToApiDto.isPresent()) {
                        // Overwrite inactive access from User with active access granted via Group
                        logger.debug("Found active API ID '{}' access from group: '{}' with status '{}' which is set as inactive API within the user",
                                groupToApi.getId().getApiId(), dbGroup.getGroupName(), true);
                        userToApiDtoList.remove(inactiveUserToApiDto.get());
                        userToApiDtoList.add(new UserToApiDto(groupToApi.getId().getApiId(), dbGroup.getGroupName(), true));
                    }
                }
            }
        }

        logger.info("====== Ending fetch API IDs from user transaction: SUCCESS ======");
        return userToApiDtoList;
    }

    @Override
    @Transactional
    public UserToApiDto updateApiFromUser(String username, Integer apiId,  UpdateUserToApiDto userToApiDto) {
        // Get Database References
        logger.info("====== Starting update API IDs from user transaction ======");
        logger.debug("Try to fetch user to API relation with name: '{}' and API ID '{}'", username, apiId);
        UserToApiId userToApiId = new UserToApiId(apiId, username);
        UserToApi userToApi = userToApiRepository.findById(userToApiId).orElseThrow();

        // Update Values
        logger.debug("Updating attributes of API within user: '{}'...", username);
        userToApi.setActive(userToApiDto.active());

        // Save
        logger.debug("Saving updated api to user within database.");
        userToApiRepository.save(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
        logger.info("====== Ending update API IDs from user transaction: SUCCESS ======");
        return new UserToApiDto(userToApi.getId().getApiId(), "user", userToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromUser(String username, Integer apiId) {
        // Get Database References
        logger.info("====== Starting delete API ID from user transaction ======");
        logger.debug("Try to fetch user to API relation with name: '{}' and API ID '{}'", username, apiId);
        UserToApiId userToApiId = new UserToApiId(apiId, username);
        UserToApi userToApi = userToApiRepository.findById(userToApiId).orElseThrow();
        userToApiRepository.delete(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
        logger.info("====== Ending delete API ID from user transaction: SUCCESS ======");
    }

    @Override
    @Transactional
    public List<GroupWithGroupRoleDto> fetchGroupListFromUser(String username) {
        logger.info("====== Starting fetch groups from user transaction ======");
        logger.debug("Try to fetch user with name: '{}'", username);
        User dbUser = userRepository.findById(username).orElseThrow();
        List<GroupWithGroupRoleDto> groupWithGroupRoleDtoList = new ArrayList<>();

        List<UserToGroup> userToGroupList = dbUser.getGroupMappings();
        for (UserToGroup userToGroup : userToGroupList) {
            logger.debug("Add group: '{}' with role '{}' to list", userToGroup.getGroup().getGroupName(),
                    userToGroup.getGroupRole());
            groupWithGroupRoleDtoList.add(new GroupWithGroupRoleDto(userToGroup.getGroup().getGroupName(),
                    userToGroup.getGroupRole()));
        }
        logger.info("====== Ending fetch groups from user transaction: SUCCESS ======");
        return groupWithGroupRoleDtoList;
    }

    /**
     * Calls the McpClient class to notify it about updated tool set of a user.
     * @param username Unique identifier of the User object in the database.
     */
    private void sendNotifyUpdatedToolsToMcpClient(String username) {
        List<Integer> apiIds = new ArrayList<>();
        for (UserToApiDto userToApiDto : fetchApiListFromUser(username, true)) {
            apiIds.add(userToApiDto.apiId());
        }
        mcpManagementClient.notifyAboutChangedToolSets(username, apiIds);
    }

    /**
     * Converts a User object from the database to an UserWithSystemRoleDto object.
     * The UserWithSystemRoleDto contains everything from a user besides the Foreign Keys to other database tables.
     * @param user USer object from database.
     * @return UserWithSystemRoleDto object
     */
    private UserWithSystemRoleDto convertUserToUserWithSystemRoleDto(User user) {
        return new UserWithSystemRoleDto(user.getUserName(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getSystemRole());
    }
}
