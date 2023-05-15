package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public abstract class LoginUser {

    private final String loginId;

    private final UserLoginType loginType;

    public abstract boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder);

    protected LoginUser(String loginId, UserLoginType loginType) {
        this.loginId = loginId;
        this.loginType = loginType;
    }
}
