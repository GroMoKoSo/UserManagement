package thm.gromokoso.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EGroupRole {
    MEMBER("Member"),
    EDITOR("Editor"),
    ADMIN("Admin");

    private final String value;

    EGroupRole(String value) {
        this.value = value;
    }

    @JsonValue   // Jackson verwendet diesen Wert bei (De-)Serialisierung
    public String getValue() {
        return value;
    }

    @JsonCreator // Jackson mappt String -> Enum
    public static EGroupRole fromValue(String value) {
        for (EGroupRole role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}

