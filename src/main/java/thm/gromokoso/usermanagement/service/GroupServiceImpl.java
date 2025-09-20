package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.model.GroupDto;
import thm.gromokoso.usermanagement.model.UserWithGroupRole;
import thm.gromokoso.usermanagement.repository.GroupRepository;
import thm.gromokoso.usermanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
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
    public void deleteGroupByGroupName(String name) {
        groupRepository.deleteById(name);
    }

    @Override
    public List<UserWithGroupRole> fetchUserListFromGroup(String name) {
        List<UserWithGroupRole> userList = new ArrayList<>();
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        List<UserToGroup> userToGroupMapping = dbGroup.getUserMappings();

        for (UserToGroup userToGroup : userToGroupMapping) {
            UserWithGroupRole userWithGroupRole = new UserWithGroupRole(userToGroup.getUser(), userToGroup.getGroupRole());
            userList.add(userWithGroupRole);
        }

        return userList;
    }

    @Override
    public GroupDto addUserToGroupList(String name, String username, EGroupRole role) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        User dbUser = userRepository.findById(username).orElseThrow();
        UserToGroup userToGroup = new UserToGroup(null, dbUser, dbGroup, role);
        dbGroup.getUserMappings().add(userToGroup);
        groupRepository.save(dbGroup);
        return convertGroupToGroupDto(dbGroup);
    }

    @Override
    public void deleteUserFromGroup(String name, String username) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        List<UserToGroup> userToGroupList = dbGroup.getUserMappings();
        userToGroupList.stream().filter(
                userToGroup -> userToGroup.getUser().getUserName().equals(username) &&
                        userToGroup.getGroup().equals(dbGroup))
                .findFirst().ifPresent(userToGroupList::remove);
    }

    @Override
    public Integer addApiIdToGroup(String name, Integer apiId) {
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        List<GroupToApi> apiAccesses = dbGroup.getApiAccesses();
        apiAccesses.add(new GroupToApi(apiId, dbGroup, true));
        dbGroup.setApiAccesses(apiAccesses);
        return apiId;
    }

    @Override
    public List<Integer> fetchApiIdListFromGroup(String name) {
        List<Integer> apiIdList = new ArrayList<>();
        Group dbGroup = groupRepository.findById(name).orElseThrow();
        for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
            apiIdList.add(groupToApi.getApiId());
        }
        return apiIdList;
    }

    @Override
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
