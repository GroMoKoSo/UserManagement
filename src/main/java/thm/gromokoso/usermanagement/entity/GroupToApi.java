package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GroupToApi {
    @Id
    private Integer apiId;
    @ManyToOne
    @JoinColumn(name="groupName", nullable = false)
    private Group group;
    private boolean active;

}