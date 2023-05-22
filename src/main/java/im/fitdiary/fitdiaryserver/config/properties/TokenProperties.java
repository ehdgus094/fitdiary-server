package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TokenProperties {

    @NotEmpty
    private final String secret;

    @NotNull
    private final Long maxAge;
}
