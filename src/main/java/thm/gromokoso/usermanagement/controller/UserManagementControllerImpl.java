package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.ESystemRole;
import thm.gromokoso.usermanagement.exception.InvalidTokenException;
import thm.gromokoso.usermanagement.exception.LastSystemAdminException;
import thm.gromokoso.usermanagement.exception.NotAuthorizedException;
import thm.gromokoso.usermanagement.exception.ResourceNotFoundException;
import thm.gromokoso.usermanagement.security.TokenProvider;
import thm.gromokoso.usermanagement.service.UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

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
    public List<UserWithSystemRoleDto> getUsers() {
        try {
            tokenProvider.getToken();
            return userService.fetchUserList();
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        }

    }

    @Override
    public UserWithSystemRoleDto addUser(@RequestBody UserDto userDto) {
        String tokenUsername;
        UserWithSystemRoleDto tokenUser;
        try {
            tokenUsername = tokenProvider.getUsernameFromToken();
            tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.saveUser(userDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            // User wants to register himself, he is registered in keycloak but not in Database
            return userService.saveUser(userDto);
        }
    }

    @Override
    public UserWithSystemRoleDto getUser(@PathVariable String username) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN && !tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.findUserByUserName(username);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserWithSystemRoleDto updateUser(@PathVariable String username,
                                            @RequestBody UpdateUserDto updateUserDto) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN && !tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.updateUser(username, updateUserDto, tokenUser.systemRole() == ESystemRole.ADMIN);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public void deleteUser(@PathVariable String username) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN && !tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            userService.deleteUserByUserName(username);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        } catch (IllegalStateException ise) {
            throw new LastSystemAdminException("Last admin in system cannot be deleted!");
        }
    }

    @Override
    public List<UserToApiDto> getApis(@PathVariable String username,
                                      @RequestParam(required = false, defaultValue = "true") boolean accessViaGroup) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN && !tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.fetchApiListFromUser(username, accessViaGroup);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserToApiDto addApis(@PathVariable String username,
                                @RequestBody UserToApiDto userToApiDto) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (!tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.addApiToUser(username, userToApiDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }

    @Override
    public UserToApiDto updateApi(@PathVariable String username,
                                  @PathVariable Integer api_id,
                                  @RequestBody UpdateUserToApiDto userToApiDto) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (!tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.updateApiFromUser(username, api_id, userToApiDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("The user either does not exists or does not have access to this API!");
        }
    }

    @Override
    public void deleteApi(@PathVariable String username,
                          @PathVariable Integer api_id) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (!tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            userService.deleteApiIdFromUser(username, api_id);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("The user either does not exists or does not have access to this API!");
        }
    }

    @Override
    public List<GroupWithGroupRoleDto> getGroups(@PathVariable String username) {
        try {
            String tokenUsername = tokenProvider.getUsernameFromToken();
            UserWithSystemRoleDto tokenUser = userService.findUserByUserName(tokenUsername);

            // Check if Requester has needed Rights
            if (tokenUser.systemRole() != ESystemRole.ADMIN && !tokenUser.userName().equals(username)) {
                throw new NotAuthorizedException("You do not have permission to access this user!");
            }
            return userService.fetchGroupListFromUser(username);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException nse) {
            throw new ResourceNotFoundException("There is no user with the name: " + username + "!");
        }
    }
}
