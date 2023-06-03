package im.fitdiary.server.auth.service.dto;

import im.fitdiary.server.auth.data.entity.AuthUser;
import im.fitdiary.server.auth.data.entity.UserLoginType;
import lombok.ToString;

@ToString(callSuper = true)
public class CreateKakaoAuthUser extends CreateAuthUser {

    public CreateKakaoAuthUser(Long userId, String loginId) {
        super(userId, loginId, UserLoginType.KAKAO);
    }

    @Override
    public AuthUser toEntity() {
        return AuthUser.createKakaoAuth(super.getUserId(), super.getLoginId());
    }
}
