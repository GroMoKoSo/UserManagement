package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import thm.gromokoso.usermanagement.entity.EGroupRole;

public record GroupWithGroupRole(
        GroupDto group,
        @Schema(example = "\"Editor\"")
        EGroupRole role
)
{ }
