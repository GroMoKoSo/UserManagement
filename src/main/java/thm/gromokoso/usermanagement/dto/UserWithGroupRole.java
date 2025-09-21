package thm.gromokoso.usermanagement.dto;

import thm.gromokoso.usermanagement.entity.EGroupRole;

public record UserWithGroupRole (UserDto user, EGroupRole groupRole){ }
