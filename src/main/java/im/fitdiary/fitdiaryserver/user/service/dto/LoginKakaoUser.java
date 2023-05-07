package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginKakaoUser extends LoginUser {

    public LoginKakaoUser(String loginId) {
        super(loginId, LoginType.KAKAO);
    }

    @Override
    public boolean hasValidPassword(String passwordFromDb, PasswordEncoder passwordEncoder) {
        return true;
    }
}
