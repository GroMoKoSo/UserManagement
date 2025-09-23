package thm.gromokoso.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ESystemRole {
    MEMBER("Member"),
    ADMIN("Admin");

    private final String value;

    ESystemRole(String value) {
        this.value = value;
    }

    @JsonValue   // Jackson verwendet diesen Wert bei (De-)Serialisierung
    public String getValue() {
        return value;
    }

    @JsonCreator // Jackson mappt String -> Enum
    public static ESystemRole fromValue(String value) {
        for (ESystemRole role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
