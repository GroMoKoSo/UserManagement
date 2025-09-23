package thm.gromokoso.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import thm.gromokoso.usermanagement.entity.EGroupType;

public record GroupDto(
        @Schema(example = "\"MyFirstGroup\"")
        String name,
        @Schema(example = "\"This description contains a lot of useful information\"")
        String description,
        @Schema(example = "\"Public\"")
        EGroupType visibility
)
{ }
