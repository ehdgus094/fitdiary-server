package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateKakaoAuthUser extends CreateAuthUser {

    public CreateKakaoAuthUser(Long userId, String loginId) {
        super(userId, loginId, UserLoginType.KAKAO);
    }

    @Override
    public void encodePassword(PasswordEncoder passwordEncoder) {
        // empty
    }

    @Override
    public AuthUser toEntity() {
        return AuthUser.createKakaoAuth(super.getUserId(), super.getLoginId());
    }
}
