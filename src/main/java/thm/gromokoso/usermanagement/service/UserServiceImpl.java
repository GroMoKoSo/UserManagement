package thm.gromokoso.usermanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.UserDto;
import thm.gromokoso.usermanagement.dto.UserToApiDto;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.entity.UserToApi;
import thm.gromokoso.usermanagement.entity.UserToGroup;
import thm.gromokoso.usermanagement.dto.GroupWithGroupRole;
import thm.gromokoso.usermanagement.repository.UserRepository;
import thm.gromokoso.usermanagement.repository.UserToApiRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToApiRepository userToApiRepository;

    public UserServiceImpl(UserRepository userRepository, UserToApiRepository userToApiRepository) { this.userRepository = userRepository; this.userToApiRepository = userToApiRepository; }

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
    @Transactional
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
    @Transactional
    public void deleteUserByUserName(String username) {
        userRepository.deleteById(username);
    }

    @Override
    @Transactional
    public UserToApiDto addApiToUser(String username, UserToApiDto userToApiIdDto) {
        User dbUser = userRepository.findById(username).orElseThrow();
        userToApiRepository.save(new UserToApi(userToApiIdDto.apiId(), dbUser, userToApiIdDto.active()));
        return userToApiIdDto;
    }

    @Override
    public List<UserToApiDto> fetchApiListFromUser(String username) {
        List<UserToApiDto> userToApiDtoList = new ArrayList<>();
        User dbUser = userRepository.findById(username).orElseThrow();
        List<UserToApi> apiAccesses = dbUser.getApiAccesses();
        for (UserToApi userToApi : apiAccesses) {
            userToApiDtoList.add(new UserToApiDto(userToApi.getApiId(), userToApi.isActive()));
        }
        return userToApiDtoList;
    }

    @Override
    @Transactional
    public UserToApiDto updateApiFromUser(String username, UserToApiDto userToApiIdDto) {
        // TODO Fix when primary key issue is resolved
        UserToApi userToApi = userToApiRepository.findById(userToApiIdDto.apiId()).orElseThrow();
        User dbUser = userRepository.findById(username).orElseThrow();
        userToApi.setApiId(userToApiIdDto.apiId());
        userToApi.setUser(dbUser);
        userToApi.setActive(userToApi.isActive());
        userToApiRepository.save(userToApi);
        return userToApiIdDto;
    }

    @Override
    @Transactional
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
            groupWithGroupRoleList.add(new GroupWithGroupRole(new GroupDto(userToGroup.getGroup().getGroupName(),
                    userToGroup.getGroup().getDescription(),
                    userToGroup.getGroup().getType()),
                    userToGroup.getGroupRole()));
        }
        return groupWithGroupRoleList;
    }

    private UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSystemRole());
    }
}
