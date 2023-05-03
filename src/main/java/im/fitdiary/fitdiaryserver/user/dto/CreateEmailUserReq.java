package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.user.entity.Gender;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.entity.UserAuth;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

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

    @Range(min = 100, max = 250, message = "height should be between 100 and 250")
    @NotNull(message = "height should not be empty")
    private Integer height;

    @Range(min = 20, max = 600, message = "weight should be between 20 and 600")
    @NotNull(message = "weight should not be empty")
    private Integer weight;

    public User toEntity() {
        return User.create(
                UserAuth.createEmailAuth(this.loginId, this.password),
                this.name,
                this.birthYmd,
                this.gender,
                this.height,
                this.weight
        );
    }
}
