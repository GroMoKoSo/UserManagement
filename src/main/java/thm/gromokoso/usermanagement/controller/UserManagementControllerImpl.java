package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.ESystemRole;
import thm.gromokoso.usermanagement.exception.InvalidTokenException;
import thm.gromokoso.usermanagement.exception.NotAuthorizedException;
import thm.gromokoso.usermanagement.exception.ResourceNotFoundException;
import thm.gromokoso.usermanagement.security.TokenProvider;
import thm.gromokoso.usermanagement.service.UserService;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserManagementControllerImpl implements UserManagementController {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    public UserManagementControllerImpl(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public List<UserDto> getUsers() {
        try {
            tokenProvider.getToken();
            return userService.fetchUserList();
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        }

    }

    @Override
    public UserDto addUser(@RequestBody UserDto user) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                return userService.saveUser(user);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        }
    }

    @Override
    public UserDto getUser(@PathVariable String username) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                return user;
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserDto updateUser(@PathVariable String username, @RequestBody UpdateUserDto updateUserDto) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                return userService.updateUser(username, updateUserDto);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public void deleteUser(@PathVariable String username) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                userService.deleteUserByUserName(username);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public List<UserToApiDto> getApis(@PathVariable String username, @RequestParam(required = false, defaultValue = "true") boolean accessViaGroup) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                return userService.fetchApiListFromUser(username, accessViaGroup);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserToApiDto addApis(@PathVariable String username, @RequestBody UserToApiDto userToApiDto) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.userName().equals(requestUser)) {
                return userService.addApiToUser(username, userToApiDto);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserToApiDto updateApi(@PathVariable String username, @PathVariable Integer api_id, @RequestBody UpdateUserToApiDto userToApiDto) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.userName().equals(requestUser)) {
                return userService.updateApiFromUser(username, api_id, userToApiDto);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("The user either does not exists or does not have access to this API!");
        }
    }

    @Override
    public void deleteApi(@PathVariable String username, @PathVariable Integer api_id) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.userName().equals(requestUser)) {
                userService.deleteApiIdFromUser(username, api_id);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("The user either does not exists or does not have access to this API!");
        }
    }

    @Override
    public List<GroupWithGroupRoleDto> getGroups(@PathVariable String username) {
        try {
            String requestUser = tokenProvider.getUsernameFromToken();
            UserDto user = userService.findUserByUserName(username);

            // Check if Requester has needed Rights
            if (user.systemRole() == ESystemRole.ADMIN || user.userName().equals(requestUser)) {
                return userService.fetchGroupListFromUser(username);
            } else {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
        } catch (AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }
}
