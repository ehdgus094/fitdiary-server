package im.fitdiary.swaggeragent.dto;

import im.fitdiary.swaggeragent.dto.handler.FieldHandler;
import im.fitdiary.swaggeragent.logger.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DtoTransformer implements AgentBuilder.Transformer {

    private final Logger logger = new Logger(getClass());

    @Override
    public Builder<?> transform(
            Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
    ) {
        for (FieldDescription.InDefinedShape field : typeDescription.getDeclaredFields()) {
            builder = new FieldHandler(builder, field, classLoader).execute();

            logger.log("[IN PROGRESS] - " + typeDescription.getSimpleName() + "." + field.getName() + " executed");
        }

        logger.log("[SUCCESS] - " + typeDescription.getSimpleName() + " successfully modified");
        return builder;
    }
}