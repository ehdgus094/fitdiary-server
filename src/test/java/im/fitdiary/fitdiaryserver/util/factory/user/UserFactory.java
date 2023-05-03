package im.fitdiary.fitdiaryserver.util.factory.user;

import im.fitdiary.fitdiaryserver.user.dto.CreateEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.Gender;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.entity.UserAuth;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class UserFactory {

    private static final String NAME = "user";
    private static final String BIRTH_YMD = "19901010";
    private static final Gender GENDER = Gender.MALE;
    private static final String LOGIN_ID = "test";
    private static final String PASSWORD = "1234";

    public static User emailUser() {
        return User.create(emailUserAuth(), NAME, BIRTH_YMD, GENDER);
    }

    public static UserAuth emailUserAuth() {
        return UserAuth.createEmailAuth(LOGIN_ID, PASSWORD);
    }

    public static CreateEmailUserReq createEmailUserReq() {
        CreateEmailUserReq req = new CreateEmailUserReq();
        setField(req, "loginId", LOGIN_ID);
        setField(req, "password", PASSWORD);
        setField(req, "name", NAME);
        setField(req, "birthYmd", BIRTH_YMD);
        setField(req, "gender", GENDER);
        return req;
    }

    public static LoginEmailUserReq loginEmailUserReq() {
        LoginEmailUserReq req = new LoginEmailUserReq();
        setField(req, "loginId", LOGIN_ID);
        setField(req, "password", PASSWORD);
        return req;
    }

    public static UserRes userRes() {
        return new UserRes(emailUser());
    }

    public static LoginUserRes loginUserRes() {
        return new LoginUserRes(
                "accessToken",
                "refreshToken",
                emailUser()
        );
    }
}
