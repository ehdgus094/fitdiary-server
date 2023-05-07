package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public abstract class LoginUser {

    private final String loginId;
    private final LoginType loginType;

    public abstract boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder);

    protected LoginUser(String loginId, LoginType loginType) {
        this.loginId = loginId;
        this.loginType = loginType;
    }
}
