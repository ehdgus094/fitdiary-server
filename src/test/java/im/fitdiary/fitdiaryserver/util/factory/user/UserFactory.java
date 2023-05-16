package im.fitdiary.fitdiaryserver.util.factory.user;

import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.dto.UserEditor;
import im.fitdiary.fitdiaryserver.user.presentation.dto.*;
import im.fitdiary.fitdiaryserver.user.service.dto.*;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class UserFactory {

    private static final String NAME = "user";

    private static final String BIRTH_YMD = "19901010";

    private static final Gender GENDER = Gender.MALE;

    private static final String EMAIL = "test@test.com";

    private static final String LOGIN_ID = "testId";

    private static final String PASSWORD = "1234";

    public static User user() {
        return User.create(
                NAME,
                BIRTH_YMD,
                GENDER,
                EMAIL
        );
    }

    public static UserEditor userEditor() {
        return new UserEditor(JsonNullable.of(NAME));
    }

    public static CreateEmailUser createEmailUser() {
        return new CreateEmailUser(EMAIL, PASSWORD, NAME, BIRTH_YMD, GENDER);
    }

    public static CreateKakaoUser createKakaoUser() {
        return new CreateKakaoUser(LOGIN_ID, NAME, BIRTH_YMD, GENDER, EMAIL);
    }

    public static CreateEmailUserReq createEmailUserReq() {
        CreateEmailUserReq req = new CreateEmailUserReq();
        setField(req, "loginId", EMAIL);
        setField(req, "password", PASSWORD);
        setField(req, "name", NAME);
        setField(req, "birthYmd", BIRTH_YMD);
        setField(req, "gender", GENDER.toString());
        return req;
    }

    public static CreateKakaoUserReq createKakaoUserReq() {
        CreateKakaoUserReq req = new CreateKakaoUserReq();
        setField(req, "loginId", LOGIN_ID);
        setField(req, "name", NAME);
        setField(req, "birthYmd", BIRTH_YMD);
        setField(req, "gender", GENDER.toString());
        setField(req, "email", EMAIL);
        return req;
    }

    public static UpdateUserReq updateUserReq() {
        UpdateUserReq req = new UpdateUserReq();
        setField(req, "name", JsonNullable.of(NAME));
        return req;
    }
}
