package im.fitdiary.fitdiaryserver.auth.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import lombok.ToString;

@ToString(callSuper = true)
public class CreateEmailAuthUser extends CreateAuthUser {

    private final String password;

    public CreateEmailAuthUser(Long userId, String loginId, String password) {
        super(userId, loginId, UserLoginType.EMAIL);
        this.password = password;
    }

    @Override
    public AuthUser toEntity() {
        return AuthUser.createEmailAuth(
                super.getUserId(),
                super.getLoginId(),
                password
        );
    }
}
