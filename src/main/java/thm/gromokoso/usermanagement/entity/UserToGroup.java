package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserToGroup {
    @EmbeddedId
    private UserToGroupId id;

    @ManyToOne
    @MapsId("userName")
    private User user;

    @ManyToOne
    @MapsId("groupName")
    private Group group;

    @Enumerated(EnumType.STRING)
    private EGroupRole groupRole;

    public UserToGroup(User user, Group group, EGroupRole role) {
        this.id = new UserToGroupId(user.getUserName(), group.getGroupName());
        this.user = user;
        this.group = group;
        this.groupRole = role;
    }
}
