package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import thm.gromokoso.usermanagement.entity.ESystemRole;

public record UserWithSystemRoleDto(
        @Schema(example = "user123")
        String userName,
        @Schema(example = "John")
        String firstName,
        @Schema(example = "Doe")
        String lastName,
        @Schema(example = "john.doe@example.com")
        String email,
        @Schema(example = "Member")
        ESystemRole systemRole
)
{ }
