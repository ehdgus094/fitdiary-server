package im.fitdiary.swaggeragent.dto.handler;

import im.fitdiary.swaggeragent.dto.annotation.SchemaBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.annotation.Annotation;

import static net.bytebuddy.dynamic.DynamicType.*;

public class FieldHandler {

    private final Builder<?> builder;

    private final FieldDescription.InDefinedShape field;

    private final ClassLoader classLoader;

    public FieldHandler(
            Builder<?> builder,
            FieldDescription.InDefinedShape field,
            ClassLoader classLoader
    ) {
        this.builder = builder;
        this.field = field;
        this.classLoader = classLoader;
    }

    public Builder<?> annotate() {
        Schema schema = new SchemaBuilder(field, classLoader).build();

        return addAnnotations(builder, field, schema);
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            FieldDescription.InDefinedShape field,
            Annotation... annotations
    ) {
        MemberAttributeExtension.ForField forField = new MemberAttributeExtension.ForField();
        for (Annotation annotation : annotations) {
            if (annotation != null) forField = forField.annotate(annotation);
        }
        return builder.visit(forField.on(ElementMatchers.named(field.getName())));
    }
}
