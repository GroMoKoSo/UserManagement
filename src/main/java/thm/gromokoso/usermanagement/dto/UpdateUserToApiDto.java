package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserToApiDto(
        @Schema(example = "true")
        boolean active
)
{ }
