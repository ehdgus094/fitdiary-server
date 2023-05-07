package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
import lombok.Getter;

@Getter
public class RefreshTokenUserRes {

    private final String accessToken;
    private final String refreshToken;

    public RefreshTokenUserRes(AuthToken authToken) {
        accessToken = authToken.getAccessToken();
        refreshToken = authToken.getRefreshToken();
    }
}
