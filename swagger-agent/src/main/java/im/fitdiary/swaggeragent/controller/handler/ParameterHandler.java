package im.fitdiary.swaggeragent.controller.handler;

import im.fitdiary.swaggeragent.controller.annotation.ParameterObjectBuilder;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.dynamic.DynamicType.*;

public class ParameterHandler {

    private final Builder<?> builder;

    private final MethodDescription.InDefinedShape method;

    private final ClassLoader classLoader;

    public ParameterHandler(
            Builder<?> builder,
            MethodDescription.InDefinedShape method,
            ClassLoader classLoader
    ) {
        this.builder = builder;
        this.method = method;
        this.classLoader = classLoader;
    }

    public Builder<?> execute() {
        Map<Integer, List<AnnotationDescription>> annotationMap = new HashMap<>();

        for (ParameterDescription.InDefinedShape parameter : method.getParameters()) {
            List<AnnotationDescription> annotations = new ArrayList<>();

            AnnotationDescription parameterObject =
                    new ParameterObjectBuilder(parameter, classLoader).build();
            annotations.add(parameterObject);

            annotationMap.put(parameter.getIndex(), annotations);
        }

        return addAnnotations(builder, method, annotationMap);
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            MethodDescription.InDefinedShape method,
            Map<Integer, List<AnnotationDescription>> annotationMap
    ) {
        MemberAttributeExtension.ForMethod forMethod = new MemberAttributeExtension.ForMethod();
        for (Integer index : annotationMap.keySet()) {
            for (AnnotationDescription annotation : annotationMap.get(index)) {
                if (annotation != null) {
                    forMethod = forMethod.annotateParameter(index, annotation);
                }
            }
        }
        return builder.visit(forMethod.on(ElementMatchers.named(method.getName())));
    }
}
