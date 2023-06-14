package im.fitdiary.swaggeragent.controlleradvice.handler;

import im.fitdiary.swaggeragent.controlleradvice.annotation.HiddenBuilder;
import io.swagger.v3.oas.annotations.Hidden;

import java.lang.annotation.Annotation;

import static net.bytebuddy.dynamic.DynamicType.*;

public class TypeHandler {

    private final Builder<?> builder;

    public TypeHandler(Builder<?> builder) {
        this.builder = builder;
    }

    public Builder<?> annotate() {
        Hidden hidden = new HiddenBuilder().build();

        return addAnnotations(builder, hidden);
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            Annotation... annotations
    ) {
        for (Annotation annotation : annotations) {
            if (annotation != null) builder = builder.annotateType(annotation);
        }
        return builder;
    }
}
