package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserToApi {
    @EmbeddedId
    private UserToApiId id;

    @ManyToOne
    @MapsId("userName")
    @JoinColumn(name = "user_name", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean active;

    public UserToApi(Integer apiId, User user, Boolean active) {
        this.id = new UserToApiId(apiId, user.getUserName());
        this.user = user;
        this.active = active;
    }
}
