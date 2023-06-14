package im.fitdiary.swaggeragent.controlleradvice.annotation;

import io.swagger.v3.oas.annotations.Hidden;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class HiddenBuilder {

    private final AnnotationDescription.Builder builder;

    public HiddenBuilder() {
        this.builder = AnnotationDescription.Builder.ofType(Hidden.class);
    }

    public Hidden build() {
        return builder.build().prepare(Hidden.class).load();
    }
}
