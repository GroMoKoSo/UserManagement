package thm.gromokoso.usermanagement.model;

import lombok.Getter;
import lombok.Setter;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.Group;

@Getter
@Setter
public class GroupWithGroupRole {
    private Group group;
    private EGroupRole role;
}
