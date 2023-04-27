package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUserRes {

    private final String accessToken;
    private final String refreshToken;
    private final UserRes user;

    @Builder
    public LoginUserRes(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = new UserRes(user);
    }
}
