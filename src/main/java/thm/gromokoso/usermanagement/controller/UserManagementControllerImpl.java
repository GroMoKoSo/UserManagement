package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.UserDto;
import thm.gromokoso.usermanagement.dto.GroupWithGroupRoleDto;
import thm.gromokoso.usermanagement.dto.UserToApiDto;
import thm.gromokoso.usermanagement.service.UserService;

import java.util.List;

@RestController
public class UserManagementControllerImpl implements UserManagementController {

    private final UserService userService;

    public UserManagementControllerImpl(UserService userService) { this.userService = userService; }

    @Override
    public List<UserDto> getUsers() { return userService.fetchUserList(); }

    @Override
    public UserDto addUser(@RequestBody UserDto user) { return userService.saveUser(user); }

    @Override
    public UserDto getUser(@PathVariable String username) { return userService.findUserByUserName(username); }

    @Override
    public UserDto updateUser(@PathVariable String username, @RequestBody UserDto user) { return userService.updateUser(user, username); }

    @Override
    public void deleteUser(@PathVariable String username) { userService.deleteUserByUserName(username); }

    @Override
    public List<UserToApiDto> getApis(@PathVariable String username, @RequestParam(required = false, defaultValue = "true") boolean accessViaGroup) { return userService.fetchApiListFromUser(username, accessViaGroup); }

    @Override
    public UserToApiDto addApis(@PathVariable String username, @RequestBody UserToApiDto userToApiDto) { return userService.addApiToUser(username, userToApiDto); }

    @Override
    public UserToApiDto updateApi(@PathVariable String username, @PathVariable Integer api_id, @RequestBody UserToApiDto userToApiDto) { return userService.updateApiFromUser(username, api_id, userToApiDto); }

    @Override
    public void deleteApi(@PathVariable String username, @PathVariable Integer api_id) { userService.deleteApiIdFromUser(username, api_id); }

    @Override
    public List<GroupWithGroupRoleDto> getGroups(@PathVariable String username) { return userService.fetchGroupListFromUser(username); }
}
