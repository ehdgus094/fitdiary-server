package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.auth.service.dto.LoginUser;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class LoginEmailUserReq {

    @NotBlank
    @Email
    private String loginId;

    @NotBlank
    private String password;

    public LoginUser toDto() {
        return new LoginUser(loginId, UserLoginType.EMAIL, password);
    }
}
