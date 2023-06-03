package im.fitdiary.server.config;

import im.fitdiary.server.common.aop.annotation.ConfigurationLogging;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationLogging
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("doc 테스트")
                        .description("desc")
                        .version("1.0.0")
                );
    }
}
