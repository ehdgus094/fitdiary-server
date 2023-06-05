package im.fitdiary.server.config;

import im.fitdiary.server.common.aop.annotation.ConfigurationLogging;
import im.fitdiary.server.security.RoleType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationLogging
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Fitdiary Server");

        Components components = new Components();

        for (RoleType roleType : RoleType.values()) {
            components.addSecuritySchemes(roleType.toString(), new SecurityScheme()
                    .name(roleType.toString())
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
            );
        }

        return new OpenAPI()
                .components(components)
                .info(info);
    }
}
