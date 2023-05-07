package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginEmailUser extends LoginUser {

    private final String password;

    public LoginEmailUser(String loginId, String password) {
        super(loginId, LoginType.EMAIL);
        this.password = password;
    }

    @Override
    public boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordFromDb);
    }
}
