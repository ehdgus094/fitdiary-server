package im.fitdiary.server.config;

import im.fitdiary.server.common.aop.annotation.ConfigurationLogging;
import im.fitdiary.server.security.RoleType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@ConfigurationLogging
@Configuration
public class SwaggerConfig {

    @Profile("local")
    @Bean
    public OpenAPI openAPILocal() {
        return swaggerConfig();
    }

    @Profile("dev")
    @Bean
    public OpenAPI openAPIDev() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("https://dev.fitdiary.im/api/v1"));
        return swaggerConfig()
                .servers(servers);
    }

    private OpenAPI swaggerConfig() {
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
