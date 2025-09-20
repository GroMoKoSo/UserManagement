package thm.gromokoso.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EGroupType {
    PRIVATE("Private"),
    PUBLIC("Public");

    private final String value;

    EGroupType(String value) {
        this.value = value;
    }

    @JsonValue   // Jackson verwendet diesen Wert bei (De-)Serialisierung
    public String getValue() {
        return value;
    }

    @JsonCreator // Jackson mappt String -> Enum
    public static EGroupType fromValue(String value) {
        for (EGroupType role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
