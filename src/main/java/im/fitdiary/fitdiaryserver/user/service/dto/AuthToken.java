package im.fitdiary.fitdiaryserver.user.service.dto;

import lombok.Getter;

@Getter
public class AuthToken {

    private final String accessToken;
    private final String refreshToken;

    public AuthToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
