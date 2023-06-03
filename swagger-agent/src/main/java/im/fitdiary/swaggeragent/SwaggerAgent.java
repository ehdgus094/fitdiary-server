package im.fitdiary.swaggeragent;

import im.fitdiary.swaggeragent.transformer.SwaggerTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class SwaggerAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                .type(isAnnotatedWith(named("io.swagger.v3.oas.annotations.tags.Tag")))
                .and(isAnnotatedWith(named("org.springframework.web.bind.annotation.RestController")))
                .transform(new SwaggerTransformer())
                .installOn(inst);
    }
}
