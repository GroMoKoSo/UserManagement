package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GroupToApiDto(
        @Schema(example = "123")
        Integer apiId,
        @Schema(example = "true")
        boolean active
)
{ }
