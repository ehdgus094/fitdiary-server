package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class LoginUser {

    private final String loginId;

    private final UserLoginType loginType;

    @Nullable
    private final String password;

    public LoginUser(String loginId, UserLoginType loginType, @Nullable String password) {
        this.loginId = loginId;
        this.loginType = loginType;
        this.password = password;
    }
}
