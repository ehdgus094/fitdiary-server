package im.fitdiary.server.user.presentation.dto;

import im.fitdiary.server.common.validation.annotation.Enum;
import im.fitdiary.server.user.data.entity.Gender;
import im.fitdiary.server.user.service.dto.CreateEmailUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
@Schema
public class CreateEmailUserReq {

    @NotBlank
    @Email
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @Pattern(regexp = "^(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])$")
    @NotNull
    private String birthYmd;

    @Enum(enumClass = Gender.class)
    @NotNull
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
