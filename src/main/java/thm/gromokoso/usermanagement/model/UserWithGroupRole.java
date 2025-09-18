package thm.gromokoso.usermanagement.model;

import lombok.Getter;
import lombok.Setter;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.User;

@Getter
@Setter
public class UserWithGroupRole {
    private User user;
    private EGroupRole groupRole;
}
