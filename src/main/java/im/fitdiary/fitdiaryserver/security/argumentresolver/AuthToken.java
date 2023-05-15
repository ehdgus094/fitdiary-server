package im.fitdiary.fitdiaryserver.security.argumentresolver;

import im.fitdiary.fitdiaryserver.security.RoleType;
import lombok.Getter;

public class AuthToken {

    @Getter
    private final Long id;

    private final RoleType roleType;

    protected AuthToken(Long id, RoleType roleType) {
        this.id = id;
        this.roleType = roleType;
    }

    public boolean has(RoleType roleType) {
        return this.roleType.equals(roleType);
    }
}
