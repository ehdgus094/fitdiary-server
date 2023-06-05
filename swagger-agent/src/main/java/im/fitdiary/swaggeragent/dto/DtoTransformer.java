package im.fitdiary.swaggeragent.dto;

import im.fitdiary.swaggeragent.logger.Logger;
import io.swagger.v3.oas.annotations.media.Schema;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.JavaModule;

import java.lang.annotation.Annotation;
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
        TypePool typePool = TypePool.Default.of(classLoader);

        for (FieldDescription.InDefinedShape field : typeDescription.getDeclaredFields()) {
            Schema schema = new SchemaBuilder(typePool, field, classLoader).build();
            builder = addAnnotations(builder, field, schema);
        }
        logger.success(typeDescription);
        return builder;
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            FieldDescription.InDefinedShape field,
            Annotation... annotations
    ) {
        MemberAttributeExtension.ForField forField = new MemberAttributeExtension.ForField();
        for (Annotation annotation : annotations) {
            forField = forField.annotate(annotation);
        }
        return builder.visit(forField.on(ElementMatchers.named(field.getName())));
    }
}