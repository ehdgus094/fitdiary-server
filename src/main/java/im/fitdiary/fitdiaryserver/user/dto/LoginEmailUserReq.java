package im.fitdiary.fitdiaryserver.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginEmailUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;
}
