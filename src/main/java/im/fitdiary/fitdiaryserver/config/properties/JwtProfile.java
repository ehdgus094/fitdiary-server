package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtProfile {

    private TokenProperties access = new TokenProperties();

    private TokenProperties refresh = new TokenProperties();
}
