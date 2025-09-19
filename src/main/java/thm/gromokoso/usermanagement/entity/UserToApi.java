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
public class UserToApi {
    @Id
    private Integer apiId;
    @ManyToOne
    @JoinColumn(name="userName", nullable = false)
    private User user;
    private boolean active;
}
