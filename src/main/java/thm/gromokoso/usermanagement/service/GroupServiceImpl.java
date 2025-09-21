package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.dto.GroupToApiDto;
import thm.gromokoso.usermanagement.dto.UserDto;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.UserWithGroupRole;
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
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        dbGroup.setGroupName(group.name());
        dbGroup.setDescription(group.description());
        dbGroup.setType(group.visibility());
        groupRepository.save(dbGroup);
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public void deleteGroupByGroupName(String name) {
        groupRepository.deleteById(name);
    }

    @Override
    public List<UserWithGroupRole> fetchUserListFromGroup(String name) {
        return userToGroupRepository.findByGroup_GroupName(name).stream()
                .map(userToGroup ->
                    new UserWithGroupRole(new UserDto(userToGroup.getUser().getUserName(),
                            userToGroup.getUser().getFirstName(),
                            userToGroup.getUser().getLastName(),
                            userToGroup.getUser().getEmail(),
                            userToGroup.getUser().getSystemRole()),
                            userToGroup.getGroupRole()))
                            .toList();

    }

    @Override
    @Transactional
    public GroupDto addUserToGroupList(String name, UserWithGroupRole userWithGroupRole) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        User dbUser = userRepository.findById(userWithGroupRole.user().userName()).orElseThrow();
        UserToGroup userToGroup = new UserToGroup(dbUser, dbGroup, userWithGroupRole.groupRole());
        userToGroupRepository.save(userToGroup);
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    @Transactional
    public void deleteUserFromGroup(String name, String username) {
        userToGroupRepository.deleteByUserUserNameAndGroupGroupName(username, name);
    }

    @Override
    @Transactional
    public GroupToApiDto addApiIdToGroup(String name, GroupToApiDto groupToApiDto) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        groupToApiRepository.save(new GroupToApi(groupToApiDto.apiId(), dbGroup, groupToApiDto.active()));
        return groupToApiDto;
    }

    @Override
    public List<GroupToApiDto> fetchApiIdListFromGroup(String name) {
        List<GroupToApiDto> groupToApiIdList = new ArrayList<>();
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
            groupToApiIdList.add(new GroupToApiDto(groupToApi.getApiId(), groupToApi.isActive()));
        }
        return groupToApiIdList;
    }

    @Override
    @Transactional
    public GroupToApiDto updateApiIdFromGroup(String name, Integer apiId, GroupToApiDto groupToApiDto) {
        // TODO fix when primary key issue is resolved
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        GroupToApi dbGroupToApi = groupToApiRepository.findById(apiId).orElseThrow();
        dbGroupToApi.setApiId(groupToApiDto.apiId());
        dbGroupToApi.setGroup(dbGroup);
        dbGroupToApi.setActive(groupToApiDto.active());
        groupToApiRepository.save(dbGroupToApi);
        return new GroupToApiDto(dbGroupToApi.getApiId(), dbGroupToApi.isActive());
    }

    @Override
    @Transactional
    public void deleteApiIdFromGroup(String name, Integer apiId) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        List<GroupToApi> apiAccesses = dbGroup.getApiAccesses();
        apiAccesses.stream().filter(
                        groupToApi -> groupToApi.getApiId().equals(apiId))
                .findFirst().ifPresent(apiAccesses::remove);
    }

    private GroupDto convertGroupToGroupDto(Group group) {
        return new GroupDto(group.getGroupName(), group.getDescription(), group.getType());
    }
}
