package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="groups")   // other name because GROUP is a SQL keyword
public class Group {
    @Id
    private String groupName;
    private String description;
    @OneToMany(mappedBy="group")
    private List<GroupToApi> apiAccesses;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToGroup> userMappings;
    @Enumerated(EnumType.STRING)
    private EGroupType type;
}
