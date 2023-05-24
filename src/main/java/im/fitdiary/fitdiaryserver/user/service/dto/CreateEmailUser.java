package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateAuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateEmailAuthUser;
import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.ToString;

@ToString(callSuper = true)
public class CreateEmailUser extends CreateUser {

    private final String password;

    public CreateEmailUser(String loginId, String password, String name, String birthYmd, Gender gender) {
        super(loginId, UserLoginType.EMAIL, name, birthYmd, gender);
        this.password = password;
    }

    @Override
    public User toUserEntity() {
        return User.create(
                super.getName(),
                super.getBirthYmd(),
                super.getGender(),
                super.getLoginId()
        );
    }

    @Override
    public CreateAuthUser toAuthUserServiceDto(Long userId) {
        return new CreateEmailAuthUser(userId, super.getLoginId(), password);
    }
}
