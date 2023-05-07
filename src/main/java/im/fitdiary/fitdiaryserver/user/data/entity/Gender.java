package im.fitdiary.fitdiaryserver.user.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender from(String value) {
        for (Gender gender : Gender.values()) {
            if (value.equalsIgnoreCase(gender.toString())) {
                return gender;
            }
        }
        return null;
    }
}
