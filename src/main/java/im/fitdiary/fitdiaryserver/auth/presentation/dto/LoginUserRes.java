package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginUserRes {

    private final String accessToken;
    private final String refreshToken;

    public LoginUserRes(JwtToken jwtToken) {
        accessToken = jwtToken.getAccessToken();
        refreshToken = jwtToken.getRefreshToken();
    }
}
