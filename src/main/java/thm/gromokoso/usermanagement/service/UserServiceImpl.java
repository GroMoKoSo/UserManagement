package thm.gromokoso.usermanagement.service;

import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.model.UserDto;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.entity.UserToApi;
import thm.gromokoso.usermanagement.entity.UserToGroup;
import thm.gromokoso.usermanagement.model.GroupWithGroupRole;
import thm.gromokoso.usermanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public UserDto saveUser(UserDto user) {
        User dbUser = new User(user.userName(), user.firstName(), user.lastName(), user.email(), new ArrayList<>(), new ArrayList<>(), user.systemRole());
        userRepository.save(dbUser);
        return convertUserToUserDto(dbUser);
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
    public UserDto updateUser(UserDto user, String username) {
        User dbUser = userRepository.findById(username).orElseThrow();
        dbUser.setUserName(user.userName());
        dbUser.setFirstName(user.firstName());
        dbUser.setLastName(user.lastName());
        dbUser.setEmail(user.email());
        dbUser.setSystemRole(user.systemRole());
        userRepository.save(dbUser);
        return convertUserToUserDto(dbUser);
    }

    @Override
    public void deleteUserByUserName(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public Integer addApiToUser(String username, Integer apiId) {
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToApi> userToApiList = dbUser.getApiAccesses();
        userToApiList.add(new UserToApi(apiId, dbUser, true));
        dbUser.setApiAccesses(userToApiList);
        userRepository.save(dbUser);
        return apiId;
    }

    @Override
    public List<Integer> fetchApiListFromUser(String username) {
        List<Integer> apiIdList = new ArrayList<>();
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        for (UserToApi userToApi : apiAccesses) {
            apiIdList.add(userToApi.getApiId());
        }
        return apiIdList;
    }

    @Override
    public void deleteApiIdFromUser(String username, Integer apiId) {
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        apiAccesses.stream().filter(
                        userToApi -> userToApi.getApiId().equals(apiId))
                .findFirst().ifPresent(apiAccesses::remove);
    }

    @Override
    public List<GroupWithGroupRole> fetchGroupListFromUser(String username) {
        List<GroupWithGroupRole> groupWithGroupRoleList = new ArrayList<>();
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToGroup> userToGroupList = dbUser.getGroupMappings();
        for (UserToGroup userToGroup : userToGroupList) {
            groupWithGroupRoleList.add(new GroupWithGroupRole(userToGroup.getGroup(), userToGroup.getGroupRole()));
        }
        return groupWithGroupRoleList;
    }

    private UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSystemRole());
    }
}
