package im.fitdiary.fitdiaryserver.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    @Builder
    public LoginUserReq(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
