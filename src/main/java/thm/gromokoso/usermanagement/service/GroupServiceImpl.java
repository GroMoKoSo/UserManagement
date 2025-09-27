package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private final McpManagementClient mcpManagementClient;

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
        Group dbGroup = new Group(groupDto.name().replace(" ", "-"), groupDto.description(), new ArrayList<>(), new ArrayList<>(), groupDto.visibility());

        if (!groupDto.name().matches("^[A-Za-z0-9-]+$")) {
            throw new InvalidNameException("Group name contains illegal characters");
        }

        groupRepository.save(dbGroup);
        return groupDto;
    }

    @Override
    public List<GroupDto> fetchGroupList(boolean privateVisibility) {
        List<GroupDto> groupList = new ArrayList<>();
        List<Group> dbGroups = groupRepository.findAll();
        for (Group dbGroup : dbGroups) {
            if (dbGroup.getType() == EGroupType.PRIVATE) {
                if (privateVisibility) {
                    groupList.add(convertGroupToGroupDto(dbGroup));
                }
            } else {
                groupList.add(convertGroupToGroupDto(dbGroup));
            }
        }
        return groupList;
    }

    @Override
    public GroupDto getGroupByGroupName(String name) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public GroupDto updateGroupByGroupName(String name, UpdateGroupDto updateGroup) {
        // Get Database References
        Group dbGroup = groupRepository.findById(name).orElseThrow();

        // Update Values
        dbGroup.setDescription(updateGroup.description());
        dbGroup.setType(updateGroup.visibility());

        // Save
        groupRepository.save(dbGroup);
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public void deleteGroupByGroupName(String name) {
        groupRepository.findById(name).orElseThrow();
        groupRepository.deleteById(name);
    }

    @Override
    @Transactional
    public List<UserWithGroupRoleDto> fetchUserListFromGroup(String name) {
        groupRepository.findById(name).orElseThrow();
        return userToGroupRepository.findByGroup_GroupName(name).stream()
                .map(userToGroup ->
                    new UserWithGroupRoleDto(userToGroup.getUser().getUserName(), userToGroup.getGroupRole())).toList();

    }

    @Override
    @Transactional
    public UserWithGroupRoleDto addUserToGroupList(String name, UserWithGroupRoleDto userWithGroupRoleDto) {
        // Get Database References
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        User dbUser = userRepository.findById(userWithGroupRoleDto.username()).orElseThrow();

        // Create new Entity
        UserToGroup userToGroup = new UserToGroup(dbUser, dbGroup, userWithGroupRoleDto.groupRole());

        // Save
        userToGroupRepository.save(userToGroup);

        // Notify MCP Management
        sendNotifyUpdatedUserToolsToMcpClient(userWithGroupRoleDto.username());
        return userWithGroupRoleDto;
    }

    @Override
    @Transactional
    public UserWithGroupRoleDto updateUserFromGroup(String name, String username, UpdateUserWithGroupRoleDto updateUserWithGroupRoleDto) {
        // Get Database References
        UserToGroupId userToGroupId = new UserToGroupId(username, name);
        UserToGroup dbUserToGroup = userToGroupRepository.findById(userToGroupId).orElseThrow();

        // Update Values
        dbUserToGroup.setGroupRole(updateUserWithGroupRoleDto.groupRole());

        // Save
        userToGroupRepository.save(dbUserToGroup);
        return new UserWithGroupRoleDto(username, updateUserWithGroupRoleDto.groupRole());
    }

    @Override
    @Transactional
    public void deleteUserFromGroup(String name, String username) {
        userToGroupRepository.findById(new UserToGroupId(username, name)).orElseThrow();

        // Delete User from Group
        userToGroupRepository.deleteByUserUserNameAndGroupGroupName(username, name);

        // Notify MCP Management
        sendNotifyUpdatedUserToolsToMcpClient(username);
    }

    @Override
    @Transactional
    public GroupToApiDto addApiIdToGroup(String name, GroupToApiDto groupToApiDto) {
        // Get Database References
        Group dbGroup = groupRepository.findById(name).orElseThrow();

        // Create new Entity
        GroupToApi groupToApi = new GroupToApi(groupToApiDto.apiId(), dbGroup, groupToApiDto.active());

        // Save
        groupToApiRepository.save(groupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
        return groupToApiDto;
    }

    @Override
    @Transactional
    public List<GroupToApiDto> fetchApiIdListFromGroup(String name) {
        List<GroupToApiDto> groupToApiIdList = new ArrayList<>();
        Group dbGroup = groupRepository.findByIdWithApis(name).orElseThrow();
        for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
            groupToApiIdList.add(new GroupToApiDto(groupToApi.getId().getApiId(), groupToApi.isActive()));
        }
        return groupToApiIdList;
    }

    @Override
    @Transactional
    public GroupToApiDto updateApiIdFromGroup(String name, Integer apiId, UpdateGroupToApiDto updateGroupToApiDto) {
        // Get Database References
        GroupToApiId groupToApiId = new GroupToApiId(apiId, name);
        GroupToApi dbGroupToApi = groupToApiRepository.findById(groupToApiId).orElseThrow();

        // Update Values
        dbGroupToApi.setActive(updateGroupToApiDto.active());

        // Save
        groupToApiRepository.save(dbGroupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
        return new GroupToApiDto(dbGroupToApi.getId().getApiId(), dbGroupToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromGroup(String name, Integer apiId) {
        GroupToApiId groupToApiId = new GroupToApiId(apiId, name);
        GroupToApi dbGroupToApi = groupToApiRepository.findById(groupToApiId).orElseThrow();
        groupToApiRepository.delete(dbGroupToApi);

        // Notify MCP Management
        sendNotifyUpdatedGroupToolsToMcpClient(name);
    }

    private void sendNotifyUpdatedGroupToolsToMcpClient(String name) {
        List<UserWithGroupRoleDto> users = fetchUserListFromGroup(name);
        for (UserWithGroupRoleDto user: users) {
            sendNotifyUpdatedUserToolsToMcpClient(user.username());
        }
    }

    private void sendNotifyUpdatedUserToolsToMcpClient(String username) {
        User dbUser = userRepository.findById(username).orElseThrow();
        List<Integer> apiIds = new ArrayList<>();
        for (UserToApi userToApi : dbUser.getApiAccesses()) {
            apiIds.add(userToApi.getId().getApiId());
        }
        mcpManagementClient.notifyAboutChangedToolSets(username, apiIds);
    }

    private GroupDto convertGroupToGroupDto(Group group) {
        return new GroupDto(group.getGroupName(), group.getDescription(), group.getType());
    }
}
