package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginKakaoUser extends LoginUser {

    public LoginKakaoUser(String loginId) {
        super(loginId, UserLoginType.KAKAO);
    }

    @Override
    public boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder) {
        return true;
    }
}
