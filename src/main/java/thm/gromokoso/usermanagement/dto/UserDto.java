package thm.gromokoso.usermanagement.dto;

import thm.gromokoso.usermanagement.entity.ESystemRole;

public record UserDto(String userName, String firstName, String lastName, String email, ESystemRole systemRole) { }
