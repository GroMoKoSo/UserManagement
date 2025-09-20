package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.model.GroupWithGroupRole;
import thm.gromokoso.usermanagement.service.UserService;

import java.util.List;

@RestController
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) { this.userService = userService; }

    @GetMapping("/users")
    public List<User> getUsers() { return userService.fetchUserList(); }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) { return userService.saveUser(user); }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) { return userService.findUserByUserName(username); }

    @PutMapping("/users/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) { return userService.updateUser(user, username); }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) { userService.deleteUserByUserName(username); }

    @GetMapping("/users/{username}/apis")
    public List<Integer> getApis(@PathVariable String username) { return userService.fetchApiListFromUser(username); }

    @PostMapping("/users/{username}/apis")
    public Integer addApis(@PathVariable String username, @RequestBody Integer api_id) { return userService.addApiToUser(username, api_id); }

    @DeleteMapping("/users/{username}/apis/{api_id}")
    public void deleteApi(@PathVariable String username, @PathVariable Integer api_id) { userService.deleteApiIdFromUser(username, api_id); }

    @GetMapping("/users/{username}/groups")
    public List<GroupWithGroupRole> getGroups(@PathVariable String username) { return userService.fetchGroupListFromUser(username); }
}
