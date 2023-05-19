package im.fitdiary.fitdiaryserver.config;

import im.fitdiary.fitdiaryserver.config.properties.JwtProperties;
import im.fitdiary.fitdiaryserver.config.properties.Mode;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter @Setter
@Builder // 테스트 객체 생성용
@AllArgsConstructor // 테스트 객체 생성용
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

    @NotNull
    private Mode mode;

    private JwtProperties jwt;
}
