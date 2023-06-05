package im.fitdiary.swaggeragent;

import im.fitdiary.swaggeragent.controlleradvice.ControllerAdviceTransformer;
import im.fitdiary.swaggeragent.dto.DtoTransformer;
import im.fitdiary.swaggeragent.controller.ControllerTransformer;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class SwaggerAgent {

    /**
     * 작동 방식:
     * 1. ControllerTransformer를 통해 @Tag 어노테이션이 있는 컨트롤러를 Swagger 스펙에 맞게 최적화 합니다.
     * 2. ControllerAdviceTransformer를 통해 RestControllerAdvice를 Swagger 스펙에서 제외합니다.
     * 3. DtoTransformer를 통해 @Schema 어노테이션이 있는 DTO를 Swagger 스펙에 맞게 최적화 합니다.
     */

    private static boolean isActive = false; // Intellij 에서 Gradle run 시 2번 실행되는거 방지

    public static void premain(String agentArgs, Instrumentation inst) {
        if (!isActive) {
            isActive = true;
            new AgentBuilder.Default()
                    .type(isAnnotatedWith(Tag.class))
                    .and(isAnnotatedWith(named("org.springframework.web.bind.annotation.RestController")))
                    .and(nameStartsWith("im.fitdiary"))
                    .transform(new ControllerTransformer())
                    .installOn(inst);

            new AgentBuilder.Default()
                    .type(isAnnotatedWith(named("org.springframework.web.bind.annotation.RestControllerAdvice")))
                    .and(nameStartsWith("im.fitdiary"))
                    .transform(new ControllerAdviceTransformer())
                    .installOn(inst);

            new AgentBuilder.Default()
                    .type(isAnnotatedWith(Schema.class))
                    .and(nameStartsWith("im.fitdiary"))
                    .transform(new DtoTransformer())
                    .installOn(inst);
        }
    }
}
