package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder // 테스트 객체 생성용
@AllArgsConstructor // 테스트 객체 생성용
@NoArgsConstructor
public class TokenProperties {

    @NotEmpty
    private String secret;

    @NotNull
    private Long maxAge;
}
