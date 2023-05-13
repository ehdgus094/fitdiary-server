package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.service.dto.LoginEmailUser;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class LoginEmailUserReq {

    @NotBlank(message = "loginId should not be empty")
    @Email(message = "incorrect email format")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    public LoginEmailUser toServiceDto() {
        return new LoginEmailUser(loginId, password);
    }
}
