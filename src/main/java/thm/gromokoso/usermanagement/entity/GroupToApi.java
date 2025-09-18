package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class GroupToApi {
    @Id
    private int apiId;
    @ManyToOne
    @JoinColumn(name="groupName", nullable = false)
    private Group group;
    private boolean active;
}