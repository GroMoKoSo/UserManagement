package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.UserDto;
import thm.gromokoso.usermanagement.dto.GroupWithGroupRole;
import thm.gromokoso.usermanagement.dto.UserToApiDto;
import thm.gromokoso.usermanagement.service.UserService;

import java.util.List;

@RestController
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) { this.userService = userService; }

    @GetMapping("/users")
    public List<UserDto> getUsers() { return userService.fetchUserList(); }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody UserDto user) { return userService.saveUser(user); }

    @GetMapping("/users/{username}")
    public UserDto getUser(@PathVariable String username) { return userService.findUserByUserName(username); }

    @PutMapping("/users/{username}")
    public UserDto updateUser(@PathVariable String username, @RequestBody UserDto user) { return userService.updateUser(user, username); }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) { userService.deleteUserByUserName(username); }

    @GetMapping("/users/{username}/apis")
    public List<UserToApiDto> getApis(@PathVariable String username) { return userService.fetchApiListFromUser(username); }

    @PostMapping("/users/{username}/apis")
    public UserToApiDto addApis(@PathVariable String username, @RequestBody UserToApiDto userToApiDto) { return userService.addApiToUser(username, userToApiDto); }

    @PutMapping("users/{username}/apis/{api_id}")
    public UserToApiDto updateApi(@PathVariable String username, @RequestBody UserToApiDto userToApiDto) { return userService.updateApiFromUser(username, userToApiDto); }

    @DeleteMapping("/users/{username}/apis/{api_id}")
    public void deleteApi(@PathVariable String username, @PathVariable Integer api_id) { userService.deleteApiIdFromUser(username, api_id); }

    @GetMapping("/users/{username}/groups")
    public List<GroupWithGroupRole> getGroups(@PathVariable String username) { return userService.fetchGroupListFromUser(username); }
}
