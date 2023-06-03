package im.fitdiary.server.auth.presentation.dto;

import im.fitdiary.server.auth.data.entity.UserLoginType;
import im.fitdiary.server.auth.service.dto.LoginUser;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class LoginKakaoUserReq {

    @NotBlank
    private String loginId;

    public LoginUser toDto() {
        return new LoginUser(loginId, UserLoginType.KAKAO, null);
    }
}
