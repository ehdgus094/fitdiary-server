package im.fitdiary.server.user.data.entity;

public enum Gender {

    MALE, FEMALE;

    public static Gender from(String value) {
        if (value == null) return null;

        for (Gender gender : Gender.values()) {
            if (value.equalsIgnoreCase(gender.toString())) {
                return gender;
            }
        }
        return null;
    }
}
