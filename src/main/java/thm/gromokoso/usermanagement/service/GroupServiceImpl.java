package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import thm.gromokoso.usermanagement.entity.*;
import thm.gromokoso.usermanagement.model.UserWithGroupRole;
import thm.gromokoso.usermanagement.repository.GroupRepository;

import java.util.ArrayList;
import java.util.List;

public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) { this.groupRepository = groupRepository; }

    @Override
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public List<Group> fetchGroupList() {
        return groupRepository.findAll();
    }

    @Override
    public Group getGroupByGroupName(String name) {
        return groupRepository.findById(name).orElseThrow();
    }

    @Override
    @Transactional
    public Group updateGroupByGroupName(Group group, String name) {
        Group dbGroup = getGroupByGroupName(name);
        dbGroup.setGroupName(group.getGroupName());
        dbGroup.setDescription(group.getDescription());
        dbGroup.setType(group.getType());
        dbGroup.setApiAccesses(group.getApiAccesses());
        dbGroup.setUserMappings(group.getUserMappings());
        return groupRepository.save(dbGroup);
    }

    @Override
    public void deleteGroupByGroupName(String name) {
        groupRepository.deleteById(name);
    }

    @Override
    public List<UserWithGroupRole> fetchUserListFromGroup(String name) {
        List<UserWithGroupRole> userList = new ArrayList<>();
        Group dbGroup = getGroupByGroupName(name);
        List<UserToGroup> userToGroupMapping = dbGroup.getUserMappings();

        for (UserToGroup userToGroup : userToGroupMapping) {
            UserWithGroupRole userWithGroupRole = new UserWithGroupRole(userToGroup.getUser(), userToGroup.getGroupRole());
            userList.add(userWithGroupRole);
        }

        return userList;
    }

    @Override
    public User addUserToGroupList(String name, User user) {
        Group dbGroup = getGroupByGroupName(name);
        UserToGroup userToGroup = new UserToGroup(null, user, dbGroup, EGroupRole.MEMBER);
        dbGroup.getUserMappings().add(userToGroup);
        groupRepository.save(dbGroup);
        return user;
    }

    @Override
    public void deleteUserFromGroup(String name, User user) {
        Group dbGroup = getGroupByGroupName(name);
        List<UserToGroup> userToGroupList = dbGroup.getUserMappings();
        userToGroupList.stream().filter(
                userToGroup -> userToGroup.getUser().equals(user) &&
                        userToGroup.getGroup().equals(dbGroup))
                .findFirst().ifPresent(userToGroupList::remove);
    }

    @Override
    public Integer addApiIdToGroup(String name, Integer apiId) {
        Group dbGroup = getGroupByGroupName(name);
        List<GroupToApi> apiAccesses = dbGroup.getApiAccesses();
        apiAccesses.add(new GroupToApi(apiId, dbGroup, true));
        dbGroup.setApiAccesses(apiAccesses);
        return apiId;
    }

    @Override
    public List<Integer> fetchApiIdListFromGroup(String name) {
        List<Integer> apiIdList = new ArrayList<>();
        Group dbGroup = getGroupByGroupName(name);
        for (GroupToApi groupToApi : dbGroup.getApiAccesses()) {
            apiIdList.add(groupToApi.getApiId());
        }
        return apiIdList;
    }

    @Override
    public void deleteApiIdFromGroup(String name, Integer apiId) {
        Group dbGroup = getGroupByGroupName(name);
        List<GroupToApi> apiAccesses = dbGroup.getApiAccesses();
        apiAccesses.stream().filter(
                        groupToApi -> groupToApi.getApiId().equals(apiId))
                .findFirst().ifPresent(apiAccesses::remove);
    }
}
