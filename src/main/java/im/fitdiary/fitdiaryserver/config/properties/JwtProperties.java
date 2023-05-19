package im.fitdiary.fitdiaryserver.config.properties;

import lombok.*;

@Getter @Setter
@Builder // 테스트 객체 생성용
@AllArgsConstructor // 테스트 객체 생성용
@NoArgsConstructor
public class JwtProperties {

    JwtProfile user;
}
