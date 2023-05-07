package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.entity.UserAuth;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateKakaoUser extends CreateUser {

    private final String email;

    public CreateKakaoUser(String loginId, String name, String birthYmd, Gender gender, String email) {
        super(loginId, LoginType.KAKAO, name, birthYmd, gender);
        this.email = email;
    }

    @Override
    public void encodePassword(PasswordEncoder passwordEncoder) {
        // empty
    }

    @Override
    public User toEntity() {
        return User.create(
                UserAuth.createKakaoAuth(super.getLoginId()),
                super.getName(),
                super.getBirthYmd(),
                super.getGender(),
                email
        );
    }
}
