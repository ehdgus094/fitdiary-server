package im.fitdiary.swaggeragent.transformer;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.*;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class SwaggerTransformer implements AgentBuilder.Transformer {

    @Override
    public Builder<?> transform(
            Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
    ) {
        return builder;
    }
}
