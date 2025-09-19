package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy="user")
    private List<UserToApi> apiAccesses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToGroup> groupMappings;
    @Enumerated(EnumType.STRING)
    private ESystemRole systemRole;
}
