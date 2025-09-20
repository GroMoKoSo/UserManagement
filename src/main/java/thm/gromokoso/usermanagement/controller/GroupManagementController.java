package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.model.GroupDto;
import thm.gromokoso.usermanagement.model.UserWithGroupRole;
import thm.gromokoso.usermanagement.service.GroupService;


import java.util.List;

@RestController
public class GroupManagementController {

    private final GroupService groupService;

    GroupManagementController(GroupService groupService) { this.groupService = groupService; }

    @GetMapping("/groups")
    public List<GroupDto> getGroups() { return groupService.fetchGroupList(); }

    @PostMapping("/groups")
    public GroupDto addGroup(@RequestBody GroupDto group) { return groupService.saveGroup(group); }

    @GetMapping("/groups/{name}")
    public GroupDto getGroup(@PathVariable String name) { return groupService.getGroupByGroupName(name); }

    @PutMapping("/groups/{name}")
    public GroupDto updateGroup(@PathVariable String name, @RequestBody GroupDto group) { return groupService.updateGroupByGroupName(group, name); }

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

    @PostMapping("/groups/{name}/users")
    public GroupDto addUserToGroup(@PathVariable String name, @RequestBody String username, @RequestBody EGroupRole role) { return groupService.addUserToGroupList(name, username, role); }

    @DeleteMapping("/groups/{name}/users/{username}")
    public void deleteUserFromGroup(@PathVariable String name, @PathVariable String username) { groupService.deleteUserFromGroup(name, username); }
}
