package im.fitdiary.fitdiaryserver.user.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRes {

    private final String accessToken;
    private final String refreshToken;

    public RefreshTokenRes(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
