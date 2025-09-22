package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class GroupToApi {
    @EmbeddedId
    private GroupToApiId id;


    @ManyToOne
    @MapsId("groupName")
    @JoinColumn(name="group_name", nullable = false)
    private Group group;
    private boolean active;

    public GroupToApi(Integer api_id, Group group, boolean active) {
        this.id = new GroupToApiId(api_id, group.getGroupName());
        this.group = group;
        this.active = active;
    }
}