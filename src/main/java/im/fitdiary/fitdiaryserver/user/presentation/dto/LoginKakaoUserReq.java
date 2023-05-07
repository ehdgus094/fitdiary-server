package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.service.dto.LoginKakaoUser;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginKakaoUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    public LoginKakaoUser toServiceDto() {
        return new LoginKakaoUser(this.loginId);
    }
}
