package im.fitdiary.server.util.factory.auth;

import im.fitdiary.server.auth.data.entity.AuthUser;
import im.fitdiary.server.auth.data.entity.UserLoginType;
import im.fitdiary.server.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.server.auth.service.dto.*;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class AuthFactory {

    private static final Long USER_ID = 1L;

    private static final String LOGIN_ID = "testId";

    private static final String LOGIN_ID_EMAIL = "test@test.com";

    private static final String PASSWORD = "1234";

    private static final String ACCESS_TOKEN = "accessToken";

    private static final String REFRESH_TOKEN = "refreshToken";

    public static AuthUser emailUser() {
        return AuthUser.createEmailAuth(USER_ID, LOGIN_ID_EMAIL, PASSWORD);
    }

    public static AuthUser kakaoUser() {
        return AuthUser.createKakaoAuth(USER_ID, LOGIN_ID);
    }

    public static CreateEmailAuthUser createEmailAuthUser() {
        return new CreateEmailAuthUser(USER_ID, LOGIN_ID_EMAIL, PASSWORD);
    }

    public static CreateKakaoAuthUser createKakaoAuthUser() {
        return new CreateKakaoAuthUser(USER_ID, LOGIN_ID);
    }

    public static LoginUser loginEmailUser() {
        return new LoginUser(LOGIN_ID_EMAIL, UserLoginType.EMAIL, PASSWORD);
    }

    public static LoginUser loginKakaoUser() {
        return new LoginUser(LOGIN_ID, UserLoginType.KAKAO, null);
    }

    public static JwtToken jwtToken() {
        return new JwtToken(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    public static LoginEmailUserReq loginEmailUserReq() {
        LoginEmailUserReq req = new LoginEmailUserReq();
        setField(req, "loginId", LOGIN_ID_EMAIL);
        setField(req, "password", PASSWORD);
        return req;
    }

    public static LoginKakaoUserReq loginKakaoUserReq() {
        LoginKakaoUserReq req = new LoginKakaoUserReq();
        setField(req, "loginId", LOGIN_ID);
        return req;
    }
}
