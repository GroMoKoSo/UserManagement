package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.UserWithGroupRole;
import thm.gromokoso.usermanagement.dto.GroupToApiDto;
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
    public List<GroupToApiDto> getApiIdsOfGroup(@PathVariable String name) { return groupService.fetchApiIdListFromGroup(name); }

    @PostMapping("/groups/{name}/apis")
    public GroupToApiDto addApiIdToGroup(@PathVariable String name, @RequestBody GroupToApiDto groupToApiDto) { return groupService.addApiIdToGroup(name, groupToApiDto); }

    @PutMapping("/groups/{name}/apis/{api_id}")
    public GroupToApiDto updateApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id, @RequestBody GroupToApiDto groupToApiDto) { return groupService.updateApiIdFromGroup(name, api_id, groupToApiDto); }

    @DeleteMapping("/groups/{name}/apis/{api_id}")
    public void deleteApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id) { groupService.deleteApiIdFromGroup(name, api_id); }

    @GetMapping("/groups/{name}/users")
    public List<UserWithGroupRole> getUsersOfGroup(@PathVariable String name) { return groupService.fetchUserListFromGroup(name); }

    @PostMapping("/groups/{name}/users")
    public GroupDto addUserToGroup(@PathVariable String name, @RequestBody UserWithGroupRole userWithGroupRole) { return groupService.addUserToGroupList(name, userWithGroupRole); }

    @DeleteMapping("/groups/{name}/users/{username}")
    public void deleteUserFromGroup(@PathVariable String name, @PathVariable String username) { groupService.deleteUserFromGroup(name, username); }
}
