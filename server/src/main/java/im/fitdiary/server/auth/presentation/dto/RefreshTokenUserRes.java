package im.fitdiary.server.auth.presentation.dto;

import im.fitdiary.server.auth.service.dto.JwtToken;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshTokenUserRes {

    private final String accessToken;
    private final String refreshToken;

    public RefreshTokenUserRes(JwtToken jwtToken) {
        accessToken = jwtToken.getAccessToken();
        refreshToken = jwtToken.getRefreshToken();
    }
}
