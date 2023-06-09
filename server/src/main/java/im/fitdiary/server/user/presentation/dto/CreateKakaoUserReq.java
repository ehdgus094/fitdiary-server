package im.fitdiary.server.user.presentation.dto;

import im.fitdiary.server.common.validation.annotation.Enum;
import im.fitdiary.server.user.data.entity.Gender;
import im.fitdiary.server.user.service.dto.CreateKakaoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@Schema
public class CreateKakaoUserReq {

    @NotBlank
    private String loginId;

    @NotBlank
    private String name;

    @Pattern(regexp = "^(19\\d{2}|20\\d{2})(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])$")
    @NotNull
    private String birthYmd;

    @Enum(enumClass = Gender.class)
    @NotNull
    private String gender;

    @NotBlank
    @Email
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
