package thm.gromokoso.usermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thm.gromokoso.usermanagement.entity.EGroupRole;
import thm.gromokoso.usermanagement.entity.Group;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupWithGroupRole {
    private Group group;
    private EGroupRole role;
}
