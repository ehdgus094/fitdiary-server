package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

@Getter
@AllArgsConstructor
public class JwtProfile {

    private final TokenProperties access;

    private final TokenProperties refresh;
}
