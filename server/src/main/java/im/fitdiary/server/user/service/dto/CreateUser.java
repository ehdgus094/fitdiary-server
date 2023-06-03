package im.fitdiary.server.user.service.dto;

import im.fitdiary.server.auth.data.entity.UserLoginType;
import im.fitdiary.server.auth.service.dto.CreateAuthUser;
import im.fitdiary.server.user.data.entity.Gender;
import im.fitdiary.server.user.data.entity.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class CreateUser {

    private final String loginId;

    private final UserLoginType loginType;

    private final String name;

    private final String birthYmd;

    private final Gender gender;

    public abstract User toUserEntity();

    public abstract CreateAuthUser toAuthUserServiceDto(Long userId);

    protected CreateUser(String loginId, UserLoginType loginType, String name, String birthYmd, Gender gender) {
        this.loginId = loginId;
        this.loginType = loginType;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
    }
}
