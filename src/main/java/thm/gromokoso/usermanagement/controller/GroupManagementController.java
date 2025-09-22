package thm.gromokoso.usermanagement.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.GroupDto;
import thm.gromokoso.usermanagement.dto.GroupToApiDto;
import thm.gromokoso.usermanagement.dto.UserWithGroupRole;

import java.util.List;

@Tag(name = "Group Management", description = "All Endpoints related to manage groups.")
public interface GroupManagementController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned all Groups",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GroupDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content)}
    )
    @GetMapping("/groups")
    List<GroupDto> getGroups();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added new Group",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content)}
    )
    @PostMapping("/groups")
    GroupDto addGroup(@RequestBody GroupDto group );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Group",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @GetMapping("/groups/{name}")
    GroupDto getGroup(@PathVariable String name);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Group",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @PutMapping("/groups/{name}")
    GroupDto updateGroup(@PathVariable String name, @RequestBody GroupDto group);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group was successfully deleted."),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
            content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @DeleteMapping("/groups/{name}")
    void deleteGroup(@PathVariable String name);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID's of Group successfully returned.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GroupToApiDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @GetMapping("/groups/{name}/apis")
    List<GroupToApiDto> getApiIdsOfGroup(@PathVariable String name);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID for Group successfully added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupToApiDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @PostMapping("/groups/{name}/apis")
    GroupToApiDto addApiIdToGroup(@PathVariable String name, @RequestBody GroupToApiDto groupToApiDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID for Group successfully updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupToApiDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name or API ID",
                    content = @Content)}
    )
    @PutMapping("/groups/{name}/apis/{api_id}")
    GroupToApiDto updateApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id, @RequestBody GroupToApiDto groupToApiDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "API ID from Group successfully deleted."),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name or API ID",
                    content = @Content)}
    )
    @DeleteMapping("/groups/{name}/apis/{api_id}")
    void deleteApiIdFromGroup(@PathVariable String name, @PathVariable Integer api_id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Users of Group successfully returned.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserWithGroupRole.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @GetMapping("/groups/{name}/users")
    List<UserWithGroupRole> getUsersOfGroup(@PathVariable String name);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully added to Group.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name",
                    content = @Content)}
    )
    @PostMapping("/groups/{name}/users")
    GroupDto addUserToGroup(@PathVariable String name, @RequestBody UserWithGroupRole userWithGroupRole);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User from Group successfully deleted."),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Group name or Username",
                    content = @Content)}
    )
    @DeleteMapping("/groups/{name}/users/{username}")
    void deleteUserFromGroup(@PathVariable String name, @PathVariable String username);
}
