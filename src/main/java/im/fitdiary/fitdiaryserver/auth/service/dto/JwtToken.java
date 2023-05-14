package im.fitdiary.fitdiaryserver.auth.service.dto;

import lombok.Getter;

@Getter
public class JwtToken {

    private final String accessToken;
    private final String refreshToken;

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}