package im.fitdiary.swaggeragent.controller.annotation;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.nullability.MaybeNull;

public class ParameterObjectBuilder {

    private final TypeDescription pageableType;

    private final TypeDescription parameterObjectType;

    private final ParameterDescription.InDefinedShape parameter;

    private final AnnotationDescription annotationDescription;

    private boolean annotationRequired = false;

    public ParameterObjectBuilder(ParameterDescription.InDefinedShape parameter, ClassLoader classLoader) {
        TypePool typePool = TypePool.Default.of(classLoader);

        pageableType =
                typePool.describe("org.springframework.data.domain.Pageable").resolve();
        parameterObjectType =
                typePool.describe("org.springdoc.api.annotations.ParameterObject").resolve();
        this.parameter = parameter;
        annotationDescription = AnnotationDescription.Builder.ofType(parameterObjectType).build();
    }

    @MaybeNull
    public AnnotationDescription build() {
        if (parameter.getType().asErasure().equals(pageableType)) {
            annotationRequired = true;
        }

        return annotationRequired
                ? annotationDescription
                : null;
    }
}
