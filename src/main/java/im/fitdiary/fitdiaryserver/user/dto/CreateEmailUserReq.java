package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.user.entity.Gender;
import im.fitdiary.fitdiaryserver.user.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.entity.UserAuth;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateEmailUserReq {

    @NotBlank(message = "loginId should not be empty")
    private String loginId;

    @NotBlank(message = "password should not be empty")
    private String password;

    @NotBlank(message = "name should not be empty")
    private String name;

    @Size(min = 8, max = 8, message = "incorrect birthYmd format")
    @NotBlank(message = "birthYmd should not be empty")
    private String birthYmd;

    @Enum(enumClass = Gender.class, message = "incorrect gender format")
    private Gender gender;

    @Min(value = 0, message = "height should be positive value")
    @NotNull(message = "height should not be empty")
    private Integer height;

    @Min(value = 0, message = "weight should be positive value")
    @NotNull(message = "weight should not be empty")
    private Integer weight;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .birthYmd(this.birthYmd)
                .gender(this.gender)
                .height(this.height)
                .weight(this.weight)
                .auth(UserAuth.builder()
                        .loginType(LoginType.EMAIL)
                        .loginId(this.loginId)
                        .password(this.password)
                        .build()
                )
                .build();
    }

    @Builder
    public CreateEmailUserReq(String loginId, String password, String name, String birthYmd, Gender gender, Integer height, Integer weight) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }
}
