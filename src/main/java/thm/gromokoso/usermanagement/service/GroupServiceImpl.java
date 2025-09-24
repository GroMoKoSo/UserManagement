package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.dto.GroupToApiDto;
import thm.gromokoso.usermanagement.dto.UserDto;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.UserWithGroupRoleDto;
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

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, UserToGroupRepository userToGroupRepository, GroupToApiRepository groupToApiRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userToGroupRepository = userToGroupRepository;
        this.groupToApiRepository = groupToApiRepository;
    }

    @Override
    @Transactional
    public GroupDto saveGroup(GroupDto group) {
        Group dbGroup = new Group(group.name(), group.description(), new ArrayList<>(), new ArrayList<>(), group.visibility());
        groupRepository.save(dbGroup);
        return group;
    }

    @Override
    public List<GroupDto> fetchGroupList() {
        List<GroupDto> groupList = new ArrayList<>();
        groupRepository.findAll().forEach(group -> groupList.add(convertGroupToGroupDto(group)));
        return groupList;
    }

    @Override
    public GroupDto getGroupByGroupName(String name) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public GroupDto updateGroupByGroupName(GroupDto group, String name) {
        // Get Database References
        Group dbGroup = groupRepository.findById(name).orElseThrow();

        // Update Values
        dbGroup.setDescription(group.description());
        dbGroup.setType(group.visibility());

        // Save
        groupRepository.save(dbGroup);
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public void deleteGroupByGroupName(String name) {
        groupRepository.deleteById(name);
    }

    @Override
    @Transactional
    public List<UserWithGroupRoleDto> fetchUserListFromGroup(String name) {
        return userToGroupRepository.findByGroup_GroupName(name).stream()
                .map(userToGroup ->
                    new UserWithGroupRoleDto(new UserDto(userToGroup.getUser().getUserName(),
                            userToGroup.getUser().getFirstName(),
                            userToGroup.getUser().getLastName(),
                            userToGroup.getUser().getEmail(),
                            userToGroup.getUser().getSystemRole()),
                            userToGroup.getGroupRole()))
                            .toList();

    }

    @Override
    @Transactional
    public UserWithGroupRoleDto addUserToGroupList(String name, UserWithGroupRoleDto userWithGroupRoleDto) {
        // Get Database References
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        User dbUser = userRepository.findById(userWithGroupRoleDto.user().userName()).orElseThrow();

        // Create new Entity
        UserToGroup userToGroup = new UserToGroup(dbUser, dbGroup, userWithGroupRoleDto.groupRole());

        // Save
        userToGroupRepository.save(userToGroup);
        return userWithGroupRoleDto;
    }

    @Override
    @Transactional
    public UserWithGroupRoleDto updateUserFromGroup(String name, String username, UserWithGroupRoleDto userWithGroupRoleDto) {
        // Get Database References
        UserToGroupId userToGroupId = new UserToGroupId(username, name);
        UserToGroup dbUserToGroup = userToGroupRepository.findById(userToGroupId).orElseThrow();

        // Update Values
        dbUserToGroup.setGroupRole(userWithGroupRoleDto.groupRole());

        // Save
        userToGroupRepository.save(dbUserToGroup);
        return userWithGroupRoleDto;
    }

    @Override
    @Transactional
    public void deleteUserFromGroup(String name, String username) {
        userToGroupRepository.deleteByUserUserNameAndGroupGroupName(username, name);
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
    public GroupToApiDto updateApiIdFromGroup(String name, Integer apiId, GroupToApiDto groupToApiDto) {
        // Get Database References
        GroupToApiId groupToApiId = new GroupToApiId(apiId, name);
        GroupToApi dbGroupToApi = groupToApiRepository.findById(groupToApiId).orElseThrow();

        // Update Values
        dbGroupToApi.setActive(groupToApiDto.active());

        // Save
        groupToApiRepository.save(dbGroupToApi);
        return new GroupToApiDto(dbGroupToApi.getId().getApiId(), dbGroupToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromGroup(String name, Integer apiId) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        List<GroupToApi> apiAccesses = dbGroup.getApiAccesses();
        apiAccesses.stream().filter(
                        groupToApi -> groupToApi.getId().getApiId().equals(apiId))
                .findFirst().ifPresent(apiAccesses::remove);
    }

    private GroupDto convertGroupToGroupDto(Group group) {
        return new GroupDto(group.getGroupName(), group.getDescription(), group.getType());
    }
}
