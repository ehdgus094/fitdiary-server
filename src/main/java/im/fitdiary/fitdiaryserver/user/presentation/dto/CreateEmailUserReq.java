package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateEmailUser;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
public class CreateEmailUserReq {

    @NotBlank(message = "loginId should not be empty")
    @Email(message = "incorrect email format")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    @NotBlank(message = "name should not be empty")
    private String name;

    @Pattern(regexp = "^(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])$", message = "incorrect birthYmd format")
    @NotNull(message = "birthYmd should not be null")
    private String birthYmd;

    @Enum(enumClass = Gender.class, message = "incorrect gender format")
    @NotNull(message = "gender should not be null")
    private String gender;

    public CreateEmailUser toDto() {
        return new CreateEmailUser(
                loginId,
                password,
                name,
                birthYmd,
                Gender.from(gender)
        );
    }
}
