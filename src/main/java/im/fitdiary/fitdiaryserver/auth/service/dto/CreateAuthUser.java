package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class CreateAuthUser {

    private final Long userId;

    private final String loginId;

    private final UserLoginType loginType;

    public abstract AuthUser toEntity();

    protected CreateAuthUser(Long userId, String loginId, UserLoginType loginType) {
        this.userId = userId;
        this.loginId = loginId;
        this.loginType = loginType;
    }
}
