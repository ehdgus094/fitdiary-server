package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.user.data.entity.Gender;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateKakaoUser;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class CreateKakaoUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    @NotBlank(message = "name should not be empty")
    private String name;

    @Pattern(regexp = "^(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])$", message = "incorrect birthYmd format")
    @NotNull(message = "birthYmd should not be null")
    private String birthYmd;

    @Enum(enumClass = Gender.class, message = "incorrect gender format")
    @NotNull(message = "gender should not be null")
    private String gender;

    @NotBlank(message = "email should not be empty")
    @Email(message = "incorrect email format")
    private String email;

    public CreateKakaoUser toDto() {
        return new CreateKakaoUser(
                loginId,
                name,
                birthYmd,
                Gender.from(gender),
                email
        );
    }
}
