package im.fitdiary.server.security;

public enum RoleType {

    ROLE_USER_ACCESS, ROLE_USER_REFRESH;

    public static RoleType from(String value) {
        if (value == null) return null;

        for (RoleType roleType : RoleType.values()) {
            if (value.equalsIgnoreCase(roleType.toString())) {
                return roleType;
            }
        }
        return null;
    }
}
