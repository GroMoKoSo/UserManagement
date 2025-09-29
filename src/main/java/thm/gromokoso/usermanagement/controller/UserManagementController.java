package thm.gromokoso.usermanagement.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.dto.*;

import java.util.List;

@Tag(name = "User Management", description = "All Endpoints related to manage users.")
public interface UserManagementController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned all Users",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = UserWithSystemRoleDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content)}
    )
    @GetMapping("/users")
    List<UserWithSystemRoleDto> getUsers();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added new User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithSystemRoleDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Data in Payload",
                    content = @Content)}
    )
    @PostMapping("/users")
    UserWithSystemRoleDto addUser(@RequestBody UserDto user);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithSystemRoleDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @GetMapping("/users/{username}")
    UserWithSystemRoleDto getUser(@PathVariable String username);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserWithSystemRoleDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @PutMapping("/users/{username}")
    UserWithSystemRoleDto updateUser(@PathVariable String username,
                                     @RequestBody UpdateUserDto user);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User was successfully deleted."),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @DeleteMapping("/users/{username}")
    void deleteUser(@PathVariable String username);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID's of User successfully returned.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserToApiDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @GetMapping("/users/{username}/apis")
    List<UserToApiDto> getApis(@PathVariable String username, @RequestParam boolean accessByGroups);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID for User successfully added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserToApiDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @PostMapping("/users/{username}/apis")
    UserToApiDto addApis(@PathVariable String username, @RequestBody UserToApiDto userToApiDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API ID for User successfully updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserToApiDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username or API ID",
                    content = @Content)}
    )
    @PutMapping("users/{username}/apis/{api_id}")
    UserToApiDto updateApi(@PathVariable String username,
                           @PathVariable Integer api_id,
                           @RequestBody UpdateUserToApiDto userToApiDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "API ID from User successfully deleted."),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username or API ID",
                    content = @Content)}
    )
    @DeleteMapping("/users/{username}/apis/{api_id}")
    void deleteApi(@PathVariable String username,
                   @PathVariable Integer api_id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Groups of User with their Role in the group successfully returned.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GroupWithGroupRoleDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized to perform this request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid Username",
                    content = @Content)}
    )
    @GetMapping("/users/{username}/groups")
    List<GroupWithGroupRoleDto> getGroups(@PathVariable String username);
}
