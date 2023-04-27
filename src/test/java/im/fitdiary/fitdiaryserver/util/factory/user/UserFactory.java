package im.fitdiary.fitdiaryserver.util.factory.user;

import im.fitdiary.fitdiaryserver.user.dto.CreateEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.entity.Gender;
import im.fitdiary.fitdiaryserver.user.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.entity.UserAuth;

public class UserFactory {

    public static User emailUser() {
        return User.builder()
                .birthYmd("19901010")
                .name("user")
                .gender(Gender.FEMALE)
                .height(160)
                .weight(50)
                .auth(auth())
                .build();
    }

    public static UserAuth auth() {
        return UserAuth.builder()
                .loginType(LoginType.EMAIL)
                .loginId("loginId")
                .password("1234")
                .build();
    }

    public static CreateEmailUserReq createEmailUserReq() {
        return CreateEmailUserReq.builder()
                .birthYmd("19901010")
                .loginId("test")
                .password("test")
                .gender(Gender.MALE)
                .height(100)
                .weight(100)
                .name("user")
                .build();
    }

    public static LoginUserReq loginUserReq() {
        return LoginUserReq.builder()
                .loginId("loginId")
                .password("1234")
                .build();
    }

    public static LoginUserRes loginUserRes() {
        return new LoginUserRes(
                "accessToken",
                "refreshToken",
                emailUser()
        );
    }
}
