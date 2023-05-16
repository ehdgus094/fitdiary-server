package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;

public class CreateKakaoAuthUser extends CreateAuthUser {

    public CreateKakaoAuthUser(Long userId, String loginId) {
        super(userId, loginId, UserLoginType.KAKAO);
    }

    @Override
    public AuthUser toEntity() {
        return AuthUser.createKakaoAuth(super.getUserId(), super.getLoginId());
    }
}
