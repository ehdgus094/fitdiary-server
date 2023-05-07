package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public abstract class CreateUser {

    private final String loginId;
    private final LoginType loginType;
    private final String name;
    private final String birthYmd;
    private final Gender gender;

    public abstract void encodePassword(PasswordEncoder passwordEncoder);

    public abstract User toEntity();

    protected CreateUser(String loginId, LoginType loginType, String name, String birthYmd, Gender gender) {
        this.loginId = loginId;
        this.loginType = loginType;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
    }
}
