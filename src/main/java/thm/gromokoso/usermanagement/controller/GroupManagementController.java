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
    public List<Group> getGroups() { return groupService.fetchGroupList(); }

    @PostMapping("/groups")
    public Group addGroup(@RequestBody Group group) { return groupService.saveGroup(group); }

    @GetMapping("/groups/{name}")
    public Group getGroup(@PathVariable String name) { return groupService.getGroupByGroupName(name); }

    @PutMapping("/groups/{name}")
    public Group updateGroup(@PathVariable String name, @RequestBody Group group) { return groupService.updateGroupByGroupName(group, name); }

    @DeleteMapping("/groups/{name}")
    public void deleteGroup(@PathVariable String name) { groupService.deleteGroupByGroupName(name); }

    @GetMapping("/groups/{name}/apis")
    public List<Integer> getApiIdsOfGroup(@PathVariable String name) { return groupService.fetchApiIdListFromGroup(name); }

    @PostMapping("/groups/{name}/apis")
    public Integer addApiIdToGroup(@PathVariable String name, @RequestBody Integer api_id) { return groupService.addApiIdToGroup(name, api_id); }

    @DeleteMapping("/groups/{name}/apis/{api_id}")
    public void deleteApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id) { groupService.deleteApiIdFromGroup(name, api_id); }

    @GetMapping("/groups/{name}/users")
    public List<UserWithGroupRole> getUsersOfGroup(@PathVariable String name) { return groupService.fetchUserListFromGroup(name); }

    // TODO add Query Parameter for Group Role
    @PostMapping("/groups/{name}/users")
    public User addUserToGroup(@PathVariable String name, @RequestBody User user) { return groupService.addUserToGroupList(name, user); }

    @DeleteMapping("/groups/{name}/users/{username}")
    public void deleteUserFromGroup(@PathVariable String name, @PathVariable String username) { groupService.deleteUserFromGroup(name, username); }
}
