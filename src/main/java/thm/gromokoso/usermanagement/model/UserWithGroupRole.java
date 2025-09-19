package thm.gromokoso.usermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithGroupRole {
    private User user;
    private EGroupRole groupRole;
}
