package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.entity.Group;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.model.UserWithGroupRole;
import thm.gromokoso.usermanagement.service.GroupService;


import java.util.List;

@RestController
public class GroupManagementController {

    private final GroupService groupService;

    GroupManagementController(GroupService groupService) { this.groupService = groupService; }

    @GetMapping("/groups")
    public Group getGroups() { /* TODO implement correctly */ return null; }

    @PostMapping("/groups")
    public Group addGroup(@RequestBody Group group) { /* TODO implement correctly */ return null; }

    @GetMapping("/groups/{name}")
    public Group getGroup(@PathVariable String name) { /* TODO implement correctly */ return null; }

    @PutMapping("/groups/{name}")
    public void updateGroup(@PathVariable String name, @RequestBody Group group) { /* TODO implement correctly */ }

    @DeleteMapping("/groups/{name}")
    public void deleteGroup(@PathVariable String name) { /* TODO implement correctly */ }

    @GetMapping("/groups/{name}/apis")
    public List<Integer> getApiIdsOfGroup(@PathVariable String name) { /* TODO implement correctly */ return null; }

    @PostMapping("/groups/{name}/apis")
    public Integer addApiIdToGroup(@PathVariable String name, @RequestBody Integer api_id) { /* TODO implement correctly */ return null; }

    @DeleteMapping("/groups/{name}/apis/{api_id}")
    public void deleteApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id) { /* TODO implement correctly */ }

    @GetMapping("/groups/{name}/users")
    public List<UserWithGroupRole> getUsersOfGroup(@PathVariable String name) { /* TODO implement correctly */ return null; }

    @PostMapping("/groups/{name}/users")
    public User addUserToGroup(@PathVariable String name, @RequestBody User user) { /* TODO implement correctly */ return null;}

    // TODO add Query Parameter for Group Role
    @DeleteMapping("/groups/{name}/users/{user_name}")
    public void deleteUserFromGroup(@PathVariable String name, @PathVariable String user_name) { /* TODO implement correctly */ }
}
