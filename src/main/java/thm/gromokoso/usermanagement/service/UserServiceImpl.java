package thm.gromokoso.usermanagement.service;

import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.entity.UserToApi;
import thm.gromokoso.usermanagement.entity.UserToGroup;
import thm.gromokoso.usermanagement.model.GroupWithGroupRole;
import thm.gromokoso.usermanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findById(userName).orElseThrow();
    }

    @Override
    public User updateUser(User user, String userName) {
        User dbUser = findUserByUserName(userName);
        dbUser.setUserName(userName);
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setEmail(user.getEmail());
        dbUser.setApiAccesses(user.getApiAccesses());
        dbUser.setGroupMappings(user.getGroupMappings());
        dbUser.setSystemRole(user.getSystemRole());
        return userRepository.save(dbUser);
    }

    @Override
    public void deleteUserByUserName(String userName) {
        userRepository.deleteById(userName);
    }

    @Override
    public Integer addApiToUser(String userName, Integer apiId) {
        User dbUser = findUserByUserName(userName);
        List<UserToApi> userToApiList = dbUser.getApiAccesses();
        userToApiList.add(new UserToApi(apiId, dbUser, true));
        dbUser.setApiAccesses(userToApiList);
        userRepository.save(dbUser);
        return apiId;
    }

    @Override
    public List<Integer> fetchApiListFromUser(String userName) {
        List<Integer> apiIdList = new ArrayList<>();
        User dbUser = findUserByUserName(userName);
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        for (UserToApi userToApi : apiAccesses) {
            apiIdList.add(userToApi.getApiId());
        }
        return apiIdList;
    }

    @Override
    public void deleteApiIdFromUser(String userName, Integer apiId) {
        User dbUser = findUserByUserName(userName);
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        apiAccesses.stream().filter(
                        userToApi -> userToApi.getApiId().equals(apiId))
                .findFirst().ifPresent(apiAccesses::remove);
    }

    @Override
    public List<GroupWithGroupRole> fetchGroupListFromUser(String userName) {
        List<GroupWithGroupRole> groupWithGroupRoleList = new ArrayList<>();
        User dbUser = findUserByUserName(userName);
        List<UserToGroup> userToGroupList = dbUser.getGroupMappings();
        for (UserToGroup userToGroup : userToGroupList) {
            groupWithGroupRoleList.add(new GroupWithGroupRole(userToGroup.getGroup(), userToGroup.getGroupRole()));
        }
        return groupWithGroupRoleList;
    }
}
