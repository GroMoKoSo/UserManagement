package thm.gromokoso.usermanagement.model;

import thm.gromokoso.usermanagement.entity.ESystemRole;

public record UserDto(String userName, String firstName, String lastName, String email, ESystemRole systemRole) { }
