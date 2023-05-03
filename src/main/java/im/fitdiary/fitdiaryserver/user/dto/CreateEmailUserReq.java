package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.user.entity.Gender;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.entity.UserAuth;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class CreateEmailUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    @NotBlank(message = "name should not be empty")
    private String name;

    @Pattern(regexp = "^(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])$", message = "incorrect birthYmd format")
    @NotNull(message = "birthYmd should not be empty")
    private String birthYmd;

    @Enum(enumClass = Gender.class, message = "incorrect gender format")
    @NotNull(message = "gender should not be empty")
    private Gender gender;

    public User toEntity() {
        return User.create(
                UserAuth.createEmailAuth(this.loginId, this.password),
                this.name,
                this.birthYmd,
                this.gender
        );
    }
}
