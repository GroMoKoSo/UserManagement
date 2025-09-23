package thm.gromokoso.usermanagement.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class serves as a combined primary key for the GroupToApi Entity.
 */
@Embeddable
@NoArgsConstructor
@Getter
public class GroupToApiId implements Serializable {
    private Integer apiId;
    private String groupName;

    public GroupToApiId(Integer apiId, String groupName) {
        this.apiId = apiId;
        this.groupName= groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupToApiId)) return false;
        GroupToApiId that = (GroupToApiId) o;
        return Objects.equals(apiId, that.apiId) &&
                Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiId, groupName);
    }
}
