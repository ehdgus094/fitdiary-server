package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenProperties {

    @NotEmpty
    private String secret;

    @NotNull
    private Long maxAge;
}
