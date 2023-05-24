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

    @NotBlank(message = "loginId should not be empty")
    @Email(message = "incorrect email format")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    public LoginUser toDto() {
        return new LoginUser(loginId, UserLoginType.EMAIL, password);
    }
}
