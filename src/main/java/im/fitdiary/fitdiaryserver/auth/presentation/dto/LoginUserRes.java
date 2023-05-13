package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.service.dto.AuthToken;
import lombok.Getter;

@Getter
public class LoginUserRes {

    private final String accessToken;
    private final String refreshToken;

    public LoginUserRes(AuthToken authToken) {
        accessToken = authToken.getAccessToken();
        refreshToken = authToken.getRefreshToken();
    }
}
