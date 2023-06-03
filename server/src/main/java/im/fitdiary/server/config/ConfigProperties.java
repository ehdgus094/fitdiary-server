package im.fitdiary.server.config;

import im.fitdiary.server.config.properties.JwtProperties;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "custom")
public class ConfigProperties {

    private final JwtProperties jwt;
}
