package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class serves as a combined primary key for the UserToGroup Entity.
 */
@Embeddable
@NoArgsConstructor
public class UserToGroupId implements Serializable {
    private String userName;
    private String groupName;

    public UserToGroupId(String userName, String groupName) {
        this.userName = userName;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserToGroupId)) return false;
        UserToGroupId that = (UserToGroupId) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, groupName);
    }
}