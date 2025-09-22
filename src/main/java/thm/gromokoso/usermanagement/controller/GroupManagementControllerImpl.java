package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.UserWithGroupRole;
import thm.gromokoso.usermanagement.dto.GroupToApiDto;
import thm.gromokoso.usermanagement.service.GroupService;


import java.util.List;

@RestController
public class GroupManagementControllerImpl implements GroupManagementController {

    private final GroupService groupService;

    GroupManagementControllerImpl(GroupService groupService) { this.groupService = groupService; }

    @Override
    public List<GroupDto> getGroups() { return groupService.fetchGroupList(); }

    @Override
    public GroupDto addGroup(@RequestBody GroupDto group) { return groupService.saveGroup(group); }

    @Override
    public GroupDto getGroup(@PathVariable String name) { return groupService.getGroupByGroupName(name); }

    @Override
    public GroupDto updateGroup(@PathVariable String name, @RequestBody GroupDto group) { return groupService.updateGroupByGroupName(group, name); }

    @Override
    public void deleteGroup(@PathVariable String name) { groupService.deleteGroupByGroupName(name); }

    @Override
    public List<GroupToApiDto> getApiIdsOfGroup(@PathVariable String name) { return groupService.fetchApiIdListFromGroup(name); }

    @Override
    public GroupToApiDto addApiIdToGroup(@PathVariable String name, @RequestBody GroupToApiDto groupToApiDto) { return groupService.addApiIdToGroup(name, groupToApiDto); }

    @Override
    public GroupToApiDto updateApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id, @RequestBody GroupToApiDto groupToApiDto) { return groupService.updateApiIdFromGroup(name, api_id, groupToApiDto); }

    @Override
    public void deleteApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id) { groupService.deleteApiIdFromGroup(name, api_id); }

    @Override
    public List<UserWithGroupRole> getUsersOfGroup(@PathVariable String name) { return groupService.fetchUserListFromGroup(name); }

    @Override
    public GroupDto addUserToGroup(@PathVariable String name, @RequestBody UserWithGroupRole userWithGroupRole) { return groupService.addUserToGroupList(name, userWithGroupRole); }

    @Override
    public void deleteUserFromGroup(@PathVariable String name, @PathVariable String username) { groupService.deleteUserFromGroup(name, username); }
}
