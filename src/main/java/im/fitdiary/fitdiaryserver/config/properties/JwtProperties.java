package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {

    JwtProfile user = new JwtProfile();
}
