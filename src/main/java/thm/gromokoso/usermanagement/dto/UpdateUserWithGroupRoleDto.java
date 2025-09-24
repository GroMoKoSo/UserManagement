package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import thm.gromokoso.usermanagement.entity.EGroupRole;

public record UpdateUserWithGroupRoleDto(
        @Schema(example = "Admin")
        EGroupRole groupRole
) { }
