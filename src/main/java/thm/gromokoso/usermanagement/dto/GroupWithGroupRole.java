package thm.gromokoso.usermanagement.dto;

import thm.gromokoso.usermanagement.entity.EGroupRole;

public record GroupWithGroupRole(GroupDto group, EGroupRole role) { }
