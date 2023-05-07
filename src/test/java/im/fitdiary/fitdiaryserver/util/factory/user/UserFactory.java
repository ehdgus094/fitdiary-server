package im.fitdiary.fitdiaryserver.util.factory.user;

import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.entity.UserAuth;
import im.fitdiary.fitdiaryserver.user.presentation.dto.*;
import im.fitdiary.fitdiaryserver.user.service.dto.*;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class UserFactory {

    private static final String NAME = "user";
    private static final String BIRTH_YMD = "19901010";
    private static final Gender GENDER = Gender.MALE;
    private static final String EMAIL = "test@test.com";
    private static final String LOGIN_ID = "testId";
    private static final String PASSWORD = "1234";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    public static UserAuth emailUserAuth() {
        return UserAuth.createEmailAuth(EMAIL, PASSWORD);
    }

    public static UserAuth kakaoUserAuth() {
        return UserAuth.createKakaoAuth(LOGIN_ID);
    }

    public static User emailUser() {
        return User.create(
                emailUserAuth(),
                NAME,
                BIRTH_YMD,
                GENDER,
                EMAIL
        );
    }

    public static User kakaoUser() {
        return User.create(
                kakaoUserAuth(),
                NAME,
                BIRTH_YMD,
                GENDER,
                EMAIL
        );
    }

    public static CreateEmailUser createEmailUser() {
        return new CreateEmailUser(EMAIL, PASSWORD, NAME, BIRTH_YMD, GENDER);
    }

    public static CreateKakaoUser createKakaoUser() {
        return new CreateKakaoUser(LOGIN_ID, NAME, BIRTH_YMD, GENDER, EMAIL);
    }

    public static LoginEmailUser loginEmailUser() {
        return new LoginEmailUser(EMAIL, PASSWORD);
    }

    public static LoginKakaoUser loginKakaoUser() {
        return new LoginKakaoUser(LOGIN_ID);
    }

    public static AuthToken authToken() {
        return new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    public static CreateEmailUserReq createEmailUserReq() {
        CreateEmailUserReq req = new CreateEmailUserReq();
        setField(req, "loginId", EMAIL);
        setField(req, "password", PASSWORD);
        setField(req, "name", NAME);
        setField(req, "birthYmd", BIRTH_YMD);
        setField(req, "gender", GENDER);
        return req;
    }

    public static CreateKakaoUserReq createKakaoUserReq() {
        CreateKakaoUserReq req = new CreateKakaoUserReq();
        setField(req, "loginId", LOGIN_ID);
        setField(req, "name", NAME);
        setField(req, "birthYmd", BIRTH_YMD);
        setField(req, "gender", GENDER);
        setField(req, "email", EMAIL);
        return req;
    }

    public static LoginEmailUserReq loginEmailUserReq() {
        LoginEmailUserReq req = new LoginEmailUserReq();
        setField(req, "loginId", EMAIL);
        setField(req, "password", PASSWORD);
        return req;
    }

    public static LoginKakaoUserReq loginKakaoUserReq() {
        LoginKakaoUserReq req = new LoginKakaoUserReq();
        setField(req, "loginId", LOGIN_ID);
        return req;
    }
}
