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
    public List<User> getUsers() { /* TODO implement correctly */ return null; }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) { /* TODO implement correctly */ return null; }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) { /* TODO implement correctly */ return null; }

    @PutMapping("/users/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) { /* TODO implement correctly */ return null; }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) { /* TODO implement correctly */ }

    @GetMapping("/users/{username}/apis")
    public List<Integer> getApis(@PathVariable String username) { /* TODO implement correctly */ return null; }

    @PostMapping("/users/{username}/apis")
    public Integer addApis(@PathVariable String username, @RequestBody Integer api_id) { /* TODO implement correctly */ return null; }

    @DeleteMapping("/users/{username}/apis/{api_id}")
    public void deleteApi(@PathVariable String username, @PathVariable Integer api_id) { /* TODO implement correctly */ }

    @GetMapping("/users/{username}/groups")
    public List<GroupWithGroupRole> getGroups(@PathVariable String username) { /* TODO implement correctly */ return null; }
}
