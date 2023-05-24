package im.fitdiary.fitdiaryserver.user.service.dto;

import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateAuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateKakaoAuthUser;
import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.ToString;

@ToString(callSuper = true)
public class CreateKakaoUser extends CreateUser {

    private final String email;

    public CreateKakaoUser(String loginId, String name, String birthYmd, Gender gender, String email) {
        super(loginId, UserLoginType.KAKAO, name, birthYmd, gender);
        this.email = email;
    }

    @Override
    public User toUserEntity() {
        return User.create(
                super.getName(),
                super.getBirthYmd(),
                super.getGender(),
                email
        );
    }

    @Override
    public CreateAuthUser toAuthUserServiceDto(Long userId) {
        return new CreateKakaoAuthUser(userId, super.getLoginId());
    }
}
