package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.auth.service.dto.LoginUser;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class LoginKakaoUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    public LoginUser toDto() {
        return new LoginUser(loginId, UserLoginType.KAKAO, null);
    }
}
