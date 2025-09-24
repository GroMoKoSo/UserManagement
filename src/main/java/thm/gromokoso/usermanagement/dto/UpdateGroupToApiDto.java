package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateGroupToApiDto(
        @Schema(example = "true")
        boolean active
)
{ }
