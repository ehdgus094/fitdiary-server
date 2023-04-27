package im.fitdiary.fitdiaryserver.config;

import im.fitdiary.fitdiaryserver.config.properties.JwtProperties;
import im.fitdiary.fitdiaryserver.config.properties.Mode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

    @NotNull
    private Mode mode;

    private JwtProperties jwt = new JwtProperties();
}
