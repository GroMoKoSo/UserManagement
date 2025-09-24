package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import thm.gromokoso.usermanagement.entity.EGroupRole;

public record GroupWithGroupRoleDto(
        @Schema(example = "\"MyFirstGroup\"")
        String groupName,
        @Schema(example = "\"Editor\"")
        EGroupRole role
)
{ }
