package thm.gromokoso.usermanagement.dto;

import thm.gromokoso.usermanagement.entity.EGroupType;

public record GroupDto(String name, String description, EGroupType visibility) { }
