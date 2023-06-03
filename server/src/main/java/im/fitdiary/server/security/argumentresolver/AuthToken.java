package im.fitdiary.server.security.argumentresolver;

import im.fitdiary.server.security.RoleType;
import lombok.Getter;
import lombok.ToString;

@ToString
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
