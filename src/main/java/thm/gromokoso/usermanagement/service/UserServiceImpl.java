package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.client.McpManagementClient;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.repository.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToApiRepository userToApiRepository;
    private final UserToGroupRepository userToGroupRepository;
    private final GroupRepository groupRepository;
    @Autowired private final McpManagementClient mcpManagementClient;

    public UserServiceImpl(UserRepository userRepository, UserToApiRepository userToApiRepository, UserToGroupRepository userToGroupRepository, GroupRepository groupRepository, McpManagementClient mcpManagementClient) {
        this.userRepository = userRepository;
        this.userToApiRepository = userToApiRepository;
        this.userToGroupRepository = userToGroupRepository;
        this.groupRepository = groupRepository;
        this.mcpManagementClient = mcpManagementClient;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User dbUser = new User(userDto.userName(), userDto.firstName(), userDto.lastName(), userDto.email(), new ArrayList<>(), new ArrayList<>(), userDto.systemRole());
        userRepository.save(dbUser);
        return userDto;
    }

    @Override
    public List<UserDto> fetchUserList() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userDtos.add(new UserDto(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSystemRole()));
        }
        return userDtos;
    }

    @Override
    public UserDto findUserByUserName(String username) {
        User user = userRepository.findById(username).orElseThrow();
        return convertUserToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(String username, UpdateUserDto updateUserDto) {
        // Get Database References
        User dbUser = userRepository.findById(username).orElseThrow();

        // Update Values
        dbUser.setFirstName(updateUserDto.firstName());
        dbUser.setLastName(updateUserDto.lastName());
        dbUser.setEmail(updateUserDto.email());
        dbUser.setSystemRole(updateUserDto.systemRole());

        // Save
        userRepository.save(dbUser);
        return convertUserToUserDto(dbUser);
    }

    @Override
    @Transactional
    public void deleteUserByUserName(String username) {
        userRepository.findById(username).orElseThrow();
        userRepository.deleteById(username);
    }

    @Override
    @Transactional
    public UserToApiDto addApiToUser(String username, UserToApiDto userToApiIdDto) {
        // Get Database References
        User dbUser = userRepository.findById(username).orElseThrow();

        // Create Entity
        UserToApi userToApi = new UserToApi(userToApiIdDto.apiId(), dbUser, userToApiIdDto.active());

        // Save
        userToApiRepository.save(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
        return userToApiIdDto;
    }

    @Override
    @Transactional
    public List<UserToApiDto> fetchApiListFromUser(String username, boolean accessViaGroup) {
        List<UserToApiDto> userToApiDtoList = new ArrayList<>();
        User dbUser = userRepository.findByIdWithApis(username).orElseThrow();
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        for (UserToApi userToApi : apiAccesses) {
            userToApiDtoList.add(new UserToApiDto(userToApi.getId().getApiId(), "user", userToApi.isActive()));
        }

        // Add API ID's from groups which the user is part of
        if (accessViaGroup) {
            List<UserToGroup> userToGroupList = userToGroupRepository.findByUser_UserName(username);
            for (UserToGroup userToGroup : userToGroupList) {
                Group dbGroup = groupRepository.findByIdWithApis(userToGroup.getGroup().getGroupName()).orElseThrow();
                for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
                    // Check whether API ID is already in an UserToApiAccess Object in List or if Group Access is active while user access in inactive
                    boolean newApiAccess = userToApiDtoList.stream().noneMatch(userToApiDto -> userToApiDto.apiId().equals(groupToApi.getId().getApiId()));
                    Optional<UserToApiDto> inactiveUserToApiDto = userToApiDtoList.stream().filter(userToApiDto -> userToApiDto.apiId().equals(groupToApi.getId().getApiId()) && !userToApiDto.active() && groupToApi.isActive()).findFirst();

                    if (newApiAccess)
                    {
                        userToApiDtoList.add(new UserToApiDto(groupToApi.getId().getApiId(), dbGroup.getGroupName(), groupToApi.isActive()));
                    } else if (inactiveUserToApiDto.isPresent()) {
                        // Overwrite inactive access from User with active access granted via Group
                        userToApiDtoList.remove(inactiveUserToApiDto.get());
                        userToApiDtoList.add(new UserToApiDto(groupToApi.getId().getApiId(), dbGroup.getGroupName(), true));
                    }
                }
            }
        }

        return userToApiDtoList;
    }

    @Override
    @Transactional
    public UserToApiDto updateApiFromUser(String username, Integer apiId,  UpdateUserToApiDto userToApiDto) {
        // Get Database References
        UserToApiId userToApiId = new UserToApiId(apiId, username);
        UserToApi userToApi = userToApiRepository.findById(userToApiId).orElseThrow();

        // Update Values
        userToApi.setActive(userToApiDto.active());

        // Save
        userToApiRepository.save(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
        return new UserToApiDto(userToApi.getId().getApiId(), "user", userToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromUser(String username, Integer apiId) {
        // Get Database References
        UserToApiId userToApiId = new UserToApiId(apiId, username);
        UserToApi userToApi = userToApiRepository.findById(userToApiId).orElseThrow();
        userToApiRepository.delete(userToApi);

        // Notify MCP Management
        sendNotifyUpdatedToolsToMcpClient(username);
    }

    @Override
    @Transactional
    public List<GroupWithGroupRoleDto> fetchGroupListFromUser(String username) {
        List<GroupWithGroupRoleDto> groupWithGroupRoleDtoList = new ArrayList<>();
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToGroup> userToGroupList = dbUser.getGroupMappings();
        for (UserToGroup userToGroup : userToGroupList) {
            groupWithGroupRoleDtoList.add(new GroupWithGroupRoleDto(userToGroup.getGroup().getGroupName(), userToGroup.getGroupRole()));
        }
        return groupWithGroupRoleDtoList;
    }

    private void sendNotifyUpdatedToolsToMcpClient(String username) {
        List<Integer> apiIds = new ArrayList<>();
        for (UserToApiDto userToApiDto : fetchApiListFromUser(username, true)) {
            apiIds.add(userToApiDto.apiId());
        }
        mcpManagementClient.notifyAboutChangedToolSets(username, apiIds);
    }

    private UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSystemRole());
    }
}
