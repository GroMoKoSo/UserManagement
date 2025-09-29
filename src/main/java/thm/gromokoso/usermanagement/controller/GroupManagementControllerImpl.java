package thm.gromokoso.usermanagement.controller;

import jakarta.persistence.EntityExistsException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.*;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.ESystemRole;
import thm.gromokoso.usermanagement.exception.InvalidTokenException;
import thm.gromokoso.usermanagement.exception.NotAuthorizedException;
import thm.gromokoso.usermanagement.exception.ResourceNotFoundException;
import thm.gromokoso.usermanagement.exception.ShouldBePutRequestException;
import thm.gromokoso.usermanagement.security.TokenProvider;
import thm.gromokoso.usermanagement.service.GroupService;
import thm.gromokoso.usermanagement.service.UserService;


import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class GroupManagementControllerImpl implements GroupManagementController {

    private final TokenProvider tokenProvider;
    private final GroupService groupService;
    private final UserService userService;

    GroupManagementControllerImpl(TokenProvider tokenprovider,
                                  GroupService groupService,
                                  UserService userService) {
        this.tokenProvider = tokenprovider;
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public List<GroupDto> getGroups() {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();

            return groupService.fetchGroupList(systemRole == ESystemRole.ADMIN);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        }
    }

    @Override
    public GroupDto addGroup(@RequestBody GroupDto group) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();

            // Check Permissions
            if (systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to add a group!");
            }
            return groupService.saveGroup(group);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (EntityExistsException eee) {
            throw new ShouldBePutRequestException("Group should be updated via PUT Request");
        }
    }

    @Override
    public GroupDto getGroup(@PathVariable String name) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole == null && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to get information about this group!");
            }
            return groupService.getGroupByGroupName(name);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        }
    }

    @Override
    public GroupDto updateGroup(@PathVariable String name,
                                @RequestBody UpdateGroupDto updateGroupDto) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to edit this group!");
            }
            return groupService.updateGroupByGroupName(name, updateGroupDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        }
    }

    @Override
    public void deleteGroup(@PathVariable String name) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to delete this group!");
            }
            groupService.deleteGroupByGroupName(name);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        }
    }

    @Override
    public List<GroupToApiDto> getApiIdsOfGroup(@PathVariable String name) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole == null && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to get information about this group!");
            }
            return groupService.fetchApiIdListFromGroup(name);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        }
    }

    @Override
    public GroupToApiDto addApiIdToGroup(@PathVariable String name,
                                         @RequestBody GroupToApiDto groupToApiDto) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && groupRole != EGroupRole.EDITOR && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to add API's of this group!");
            }
            return groupService.addApiIdToGroup(name, groupToApiDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        } catch (EntityExistsException eee) {
            throw new ShouldBePutRequestException("Api of group should be updated via PUT Request");
        }
    }

    @Override
    public GroupToApiDto updateApiIdFromGroup(@PathVariable String name,
                                              @PathVariable Integer api_id,
                                              @RequestBody UpdateGroupToApiDto groupToApiDto) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && groupRole != EGroupRole.EDITOR && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to edit API's of this group!");
            }
            return groupService.updateApiIdFromGroup(name, api_id, groupToApiDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group does not exist or does not have access to the API ID!");
        }
    }

    @Override
    public void deleteApiIdFromGroup(@PathVariable String name,
                                     @PathVariable Integer api_id) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && groupRole != EGroupRole.EDITOR && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to delete API's of this group!");
            }
            groupService.deleteApiIdFromGroup(name, api_id);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group does not exist or does not have access to the API ID!");
        }
    }

    @Override
    public List<UserWithGroupRoleDto> getUsersOfGroup(@PathVariable String name) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole == null && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to get information about the users of this group!");
            }
            return groupService.fetchUserListFromGroup(name);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        }
    }

    @Override
    public UserWithGroupRoleDto addUserToGroup(@PathVariable String name,
                                               @RequestBody UserWithGroupRoleDto userWithGroupRoleDto) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to add users to this group!");
            }
            return groupService.addUserToGroupList(name, userWithGroupRoleDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group not found!");
        } catch (EntityExistsException eee) {
            throw new ShouldBePutRequestException("User of group should be updated via PUT Request");
        }
    }

    @Override
    public UserWithGroupRoleDto updateUserFromGroup(@PathVariable String name,
                                                    @PathVariable String username,
                                                    @RequestBody UpdateUserWithGroupRoleDto userWithGroupRoleDto) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && systemRole != ESystemRole.ADMIN) {
                throw new NotAuthorizedException("You do not have permission to edit userdata to this group!");
            }
            return groupService.updateUserFromGroup(name, username, userWithGroupRoleDto);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group does not exist or user is not a Member!");
        }
    }

    @Override
    public void deleteUserFromGroup(@PathVariable String name,
                                    @PathVariable String username) {
        try {
            ESystemRole systemRole = getSystemRoleOfRequester();
            EGroupRole groupRole = getGroupRoleOfUser(name);
            String tokenUsername = tokenProvider.getUsernameFromToken();

            // Check Permissions
            if (groupRole != EGroupRole.ADMIN && systemRole != ESystemRole.ADMIN && !tokenUsername.equals(username)) {
                throw new NotAuthorizedException("You do not have permission to delete users from this group!");
            }
            groupService.deleteUserFromGroup(name, username);
        } catch (OAuth2AuthenticationException ae) {
            throw new InvalidTokenException("The authentication token is invalid!");
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Group does not exist or user is not a Member!");
        }
    }

    private EGroupRole getGroupRoleOfUser(String name) throws OAuth2AuthenticationException {
        String username = tokenProvider.getUsernameFromToken();
        for (GroupWithGroupRoleDto groupWithGroupRoleDto : userService.fetchGroupListFromUser(username)) {
            if (groupWithGroupRoleDto.groupName().equals(name)) {
                return groupWithGroupRoleDto.role();
            }
        }

        return null;
    }

    private ESystemRole getSystemRoleOfRequester() throws OAuth2AuthenticationException {
        return userService.findUserByUserName(tokenProvider.getUsernameFromToken()).systemRole();
    }
}
