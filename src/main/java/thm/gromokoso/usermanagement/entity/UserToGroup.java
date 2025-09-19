package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserToGroup {
    @EmbeddedId
    private UserToGroupId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("groupId")
    private Group group;

    @Enumerated(EnumType.STRING)
    private EGroupRole groupRole;
}
