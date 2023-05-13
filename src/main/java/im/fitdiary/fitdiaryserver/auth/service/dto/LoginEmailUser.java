package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginEmailUser extends LoginUser {

    private final String password;

    public LoginEmailUser(String loginId, String password) {
        super(loginId, UserLoginType.EMAIL);
        this.password = password;
    }

    @Override
    public boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordFromDb);
    }
}
