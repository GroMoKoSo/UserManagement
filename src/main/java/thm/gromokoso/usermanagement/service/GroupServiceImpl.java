package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.client.McpManagementClient;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.exception.InvalidNameException;
import thm.gromokoso.usermanagement.repository.GroupRepository;
import thm.gromokoso.usermanagement.repository.GroupToApiRepository;
import thm.gromokoso.usermanagement.repository.UserRepository;
import thm.gromokoso.usermanagement.repository.UserToGroupRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserToGroupRepository userToGroupRepository;
    private final GroupToApiRepository groupToApiRepository;
    private final McpManagementClient mcpManagementClient;

    Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, UserToGroupRepository userToGroupRepository, GroupToApiRepository groupToApiRepository, McpManagementClient mcpManagementClient) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userToGroupRepository = userToGroupRepository;
        this.groupToApiRepository = groupToApiRepository;
        this.mcpManagementClient = mcpManagementClient;
    }

    @Override
    @Transactional
    public GroupDto saveGroup(GroupDto groupDto) {
        logger.info("====== Starting save group transaction ======");

        logger.debug("Replacing whitespaces of group name with '-' characters.");
        Group dbGroup = new Group(groupDto.name().replace(" ", "-"), groupDto.description(), new ArrayList<>(), new ArrayList<>(), groupDto.visibility());

        if (!groupDto.name().matches("^[A-Za-z0-9-]+$")) {
            logger.error("Group name contains not allowed characters. Allowed are 'A-Z', 'a-z', '0-9' and '-'. Aborting Transaction...");
            throw new InvalidNameException("Group name contains illegal characters");
        }

        logger.debug("Saving group to database.");
        groupRepository.save(dbGroup);
        logger.info("====== Ending save group transaction: SUCCESS ======");
        return groupDto;
    }

    @Override
    public List<GroupDto> fetchGroupList(boolean privateVisibility) {
        logger.info("====== Starting to fetch groups ======");
        List<GroupDto> groupList = new ArrayList<>();
        EGroupType groupVisibility = privateVisibility ? EGroupType.PRIVATE : EGroupType.PUBLIC;
        if (privateVisibility) logger.debug("Also fetching groups with private visibility");
        List<Group> dbGroups = groupRepository.findAllByTypeVisible(groupVisibility);
        for (Group dbGroup : dbGroups) {
            groupList.add(convertGroupToGroupDto(dbGroup));
        }
        logger.info("====== Ending to fetch groups: SUCCESS ======");
        return groupList;
    }

    @Override
    public GroupDto getGroupByGroupName(String name) {
        logger.info("====== Starting to fetch group by name ======");
        logger.debug("Try to fetch group with name: '{}'", name);
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        logger.info("====== Ending to fetch group by name: SUCCESS ======");
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public GroupDto updateGroupByGroupName(String name, UpdateGroupDto updateGroup) {
        // Get Database References
        logger.info("====== Starting update group transaction ======");
        Group dbGroup = groupRepository.findById(name).orElseThrow();

        // Update Values
        logger.debug("Updating attributes of group: '{}'...", name);
        dbGroup.setDescription(updateGroup.description());
        dbGroup.setType(updateGroup.visibility());

        // Save
        logger.debug("Saving updated group to database.");
        groupRepository.save(dbGroup);
        logger.info("====== Ending update group transaction ======");
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public void deleteGroupByGroupName(String name) {
        logger.info("====== Starting delete group transaction ======");
        groupRepository.findById(name).orElseThrow();
        groupRepository.deleteById(name);
        logger.info("====== Ending delete group transaction: SUCCESS ======");
    }

    @Override
    @Transactional
    public List<UserWithGroupRoleDto> fetchUserListFromGroup(String name) {
        logger.info("====== Starting fetch user from group transaction ======");
        logger.debug("Try to fetch group with name: '{}'", name);
        groupRepository.findById(name).orElseThrow();
        ArrayList<UserWithGroupRoleDto> userWithGroupRoleList = new ArrayList<>();
        logger.debug("Try to fetch user to group relations via group name: '{}'", name);
        List<UserToGroup> userToGroupList = userToGroupRepository.findByGroup_GroupName(name);
        for (UserToGroup userToGroup : userToGroupList) {
            logger.debug("Add user: '{}' with role '{}' to list", userToGroup.getUser().getUserName(), userToGroup.getGroupRole());
            userWithGroupRoleList.add(new UserWithGroupRoleDto(userToGroup.getUser().getUserName(), userToGroup.getGroupRole()));
        }
        logger.info("====== Ending fetch user from group transaction: SUCCESS ======");
        return userWithGroupRoleList;
    }

    @Override
    @Transactional
    public UserWithGroupRoleDto addUserToGroupList(String name, UserWithGroupRoleDto userWithGroupRoleDto) {
        logger.info("====== Starting add user from group transaction ======");
        // Get Database References
        logger.debug("Try to fetch group with name: '{}'", name);
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        logger.debug("Try to fetch user with name: '{}'", userWithGroupRoleDto.username());
        User dbUser = userRepository.findById(userWithGroupRoleDto.username()).orElseThrow();

        // Create new Entity
        UserToGroup userToGroup = new UserToGroup(dbUser, dbGroup, userWithGroupRoleDto.groupRole());

        // Save
        logger.debug("Saving user to group within database.");
        userToGroupRepository.save(userToGroup);

        // Notify MCP Management
        sendNotifyUpdatedUserToolsToMcpClient(userWithGroupRoleDto.username());
        logger.info("====== Ending add user from group transaction: SUCCESS ======");
        return userWithGroupRoleDto;
    }

    @Override
    @Transactional
    public UserWithGroupRoleDto updateUserFromGroup(String name, String username, UpdateUserWithGroupRoleDto updateUserWithGroupRoleDto) {
        logger.info("====== Starting update user from group transaction ======");
        // Get Database References
        logger.debug("Try to fetch user to group relation with username: '{}' and group name '{}'", username, name);
        UserToGroupId userToGroupId = new UserToGroupId(username, name);
        UserToGroup dbUserToGroup = userToGroupRepository.findById(userToGroupId).orElseThrow();

        // Update Values
        logger.debug("Updating attributes of user within group: '{}'...", name);
        dbUserToGroup.setGroupRole(updateUserWithGroupRoleDto.groupRole());

        // Save
        logger.debug("Saving updated user to group within database.");
        userToGroupRepository.save(dbUserToGroup);
        logger.info("====== Ending update user from group transaction: SUCCESS ======");
        return new UserWithGroupRoleDto(username, updateUserWithGroupRoleDto.groupRole());
    }

    @Override
    @Transactional
    public void deleteUserFromGroup(String name, String username) {
        logger.info("====== Starting delete user from group transaction ======");
        logger.debug("Try to fetch user to group relation with username: '{}' and group name '{}'", username, name);
        userToGroupRepository.findById(new UserToGroupId(username, name)).orElseThrow();

        // Delete User from Group
        userToGroupRepository.deleteByUserUserNameAndGroupGroupName(username, name);

        // Notify MCP Management
        sendNotifyUpdatedUserToolsToMcpClient(username);
        logger.info("====== Ending delete user from group transaction: SUCCESS ======");
    }

    @Override
    @Transactional
    public GroupToApiDto addApiIdToGroup(String name, GroupToApiDto groupToApiDto) {
        // Get Database References
        logger.info("====== Starting add API ID to group transaction ======");
        logger.debug("Try to fetch group with name: '{}'", name);
        Group dbGroup = groupRepository.findById(name).orElseThrow();

        // Create new Entity
        GroupToApi groupToApi = new GroupToApi(groupToApiDto.apiId(), dbGroup, groupToApiDto.active());

        // Save
        logger.debug("Saving new API with ID: '{}' to group with name: '{}'", groupToApiDto.apiId(), name);
        groupToApiRepository.save(groupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
        logger.info("====== Ending add API ID to group transaction: SUCCESS ======");
        return groupToApiDto;
    }

    @Override
    @Transactional
    public List<GroupToApiDto> fetchApiIdListFromGroup(String name) {
        logger.info("====== Starting fetch API IDs from group transaction ======");
        logger.debug("Try to fetch group with name: '{}'", name);
        Group dbGroup = groupRepository.findByIdWithApis(name).orElseThrow();
        List<GroupToApiDto> groupToApiIdList = new ArrayList<>();
        for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
            logger.debug("Add Api ID: '{}' to group '{}' to list", groupToApi.getId().getApiId(), groupToApi.isActive());
            groupToApiIdList.add(new GroupToApiDto(groupToApi.getId().getApiId(), groupToApi.isActive()));
        }
        logger.info("====== Ending fetch API IDs from group transaction: SUCCESS ======");
        return groupToApiIdList;
    }

    @Override
    @Transactional
    public GroupToApiDto updateApiIdFromGroup(String name, Integer apiId, UpdateGroupToApiDto updateGroupToApiDto) {
        // Get Database References
        logger.info("====== Starting update API IDs from group transaction ======");
        logger.debug("Try to fetch group to API relation with name: '{}' and API ID '{}'", name, apiId);
        GroupToApiId groupToApiId = new GroupToApiId(apiId, name);
        GroupToApi dbGroupToApi = groupToApiRepository.findById(groupToApiId).orElseThrow();

        // Update Values
        logger.debug("Updating attributes of API within group: '{}'...", name);
        dbGroupToApi.setActive(updateGroupToApiDto.active());

        // Save
        logger.debug("Saving updated api to group within database.");
        groupToApiRepository.save(dbGroupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
        logger.info("====== Ending update API IDs from group transaction: SUCCESS ======");
        return new GroupToApiDto(dbGroupToApi.getId().getApiId(), dbGroupToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromGroup(String name, Integer apiId) {
        logger.info("====== Starting delete API ID from group transaction ======");
        logger.debug("Try to fetch group to API relation with name: '{}' and API ID '{}'", name, apiId);
        GroupToApiId groupToApiId = new GroupToApiId(apiId, name);
        GroupToApi dbGroupToApi = groupToApiRepository.findById(groupToApiId).orElseThrow();
        groupToApiRepository.delete(dbGroupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
        logger.info("====== Ending delete API ID from group transaction: SUCCESS ======");
    }

    /**
     * Calls the McpClient class to notify it about updated tool set of user within the given group.
     * @param name Unique identifier of the Group object in the database.
     */
    private void sendNotifyUpdatedGroupToolsToMcpClient(String name) {
        List<UserWithGroupRoleDto> users = fetchUserListFromGroup(name);
        for (UserWithGroupRoleDto user: users) {
            sendNotifyUpdatedUserToolsToMcpClient(user.username());
        }
    }

    /**
     * Calls the McpClient class to notify it about updated tool set of a user.
     * @param username Unique identifier of the User object in the database.
     */
    private void sendNotifyUpdatedUserToolsToMcpClient(String username) {
        User dbUser = userRepository.findById(username).orElseThrow();
        List<Integer> apiIds = new ArrayList<>();
        for (UserToApi userToApi : dbUser.getApiAccesses()) {
            apiIds.add(userToApi.getId().getApiId());
        }
        mcpManagementClient.notifyAboutChangedToolSets(username, apiIds);
    }

    /**
     * Converts a Group object from the database to an GroupDto object.
     * The GroupDto contains everything from a group besides the Foreign Keys to other database tables.
     * @param group Group object from database.
     * @return GroupDto object
     */
    private GroupDto convertGroupToGroupDto(Group group) {
        return new GroupDto(group.getGroupName(), group.getDescription(), group.getType());
    }
}
