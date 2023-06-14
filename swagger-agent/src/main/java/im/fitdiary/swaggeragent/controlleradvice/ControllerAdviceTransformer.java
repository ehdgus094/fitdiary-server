package im.fitdiary.swaggeragent.controlleradvice;

import im.fitdiary.swaggeragent.controlleradvice.handler.TypeHandler;
import im.fitdiary.swaggeragent.logger.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class ControllerAdviceTransformer implements AgentBuilder.Transformer {

    private final Logger logger = new Logger(getClass());

    @Override
    public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
    ) {
        builder = new TypeHandler(builder).annotate();

        logger.log("[SUCCESS] - " + typeDescription.getSimpleName() + " successfully modified");
        return builder;
    }
}
