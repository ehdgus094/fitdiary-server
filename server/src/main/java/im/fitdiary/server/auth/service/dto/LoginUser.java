package im.fitdiary.server.auth.service.dto;

import im.fitdiary.server.auth.data.entity.UserLoginType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
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
