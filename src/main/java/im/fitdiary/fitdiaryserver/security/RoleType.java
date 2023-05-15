package im.fitdiary.fitdiaryserver.security;

public enum RoleType {

    ROLE_USER_ACCESS, ROLE_USER_REFRESH;

    public static RoleType from(String value) {
        for (RoleType roleType : RoleType.values()) {
            if (value.equalsIgnoreCase(roleType.toString())) {
                return roleType;
            }
        }
        return null;
    }
}
