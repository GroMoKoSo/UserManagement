package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class serves as a combined primary key for the UserToApi Entity.
 */
@Embeddable
@NoArgsConstructor
@Getter
public class UserToApiId implements Serializable {
    private Integer apiId;
    private String userName;

    public UserToApiId(Integer apiId, String userName) {
        this.apiId = apiId;
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserToApiId)) return false;
        UserToApiId that = (UserToApiId) o;
        return Objects.equals(apiId, that.apiId) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiId, userName);
    }
}
