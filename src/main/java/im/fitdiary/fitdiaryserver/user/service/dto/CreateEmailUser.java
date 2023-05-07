package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.entity.UserAuth;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateEmailUser extends CreateUser {

    private String password;

    public CreateEmailUser(String loginId, String password, String name, String birthYmd, Gender gender) {
        super(loginId, LoginType.EMAIL, name, birthYmd, gender);
        this.password = password;
    }

    @Override
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    @Override
    public User toEntity() {
        return User.create(
                UserAuth.createEmailAuth(
                        super.getLoginId(),
                        this.password
                ),
                super.getName(),
                super.getBirthYmd(),
                super.getGender(),
                super.getLoginId()
        );
    }
}
