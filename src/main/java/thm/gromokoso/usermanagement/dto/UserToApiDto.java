package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserToApiDto(
        @Schema(example = "123")
        Integer apiId,
        @Schema(example = "user", description = "Where the user got the access from. This is either 'user' or the name of the Group which grants the user access." )
        String accessVia,
        @Schema(example = "true")
        boolean active
)
{ }
