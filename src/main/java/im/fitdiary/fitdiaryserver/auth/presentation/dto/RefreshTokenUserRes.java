package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import lombok.Getter;

@Getter
public class RefreshTokenUserRes {

    private final String accessToken;
    private final String refreshToken;

    public RefreshTokenUserRes(JwtToken jwtToken) {
        accessToken = jwtToken.getAccessToken();
        refreshToken = jwtToken.getRefreshToken();
    }
}
