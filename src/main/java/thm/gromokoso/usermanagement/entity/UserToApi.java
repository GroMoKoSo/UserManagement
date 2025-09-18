package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class UserToApi {
    @Id
    private int apiId;
    @ManyToOne
    @JoinColumn(name="userName", nullable = false)
    private User user;
    private boolean active;
}
