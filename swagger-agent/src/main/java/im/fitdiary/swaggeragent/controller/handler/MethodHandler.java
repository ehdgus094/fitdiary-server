package im.fitdiary.swaggeragent.controller.handler;

import im.fitdiary.swaggeragent.controller.annotation.ApiResponsesBuilder;
import im.fitdiary.swaggeragent.controller.annotation.ParametersBuilder;
import im.fitdiary.swaggeragent.controller.annotation.SecurityRequirementsBuilder;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.annotation.Annotation;

import static net.bytebuddy.dynamic.DynamicType.*;

public class MethodHandler {

    private final Builder<?> builder;

    private final MethodDescription.InDefinedShape method;

    private final ClassLoader classLoader;

    public MethodHandler(
            Builder<?> builder,
            MethodDescription.InDefinedShape method,
            ClassLoader classLoader
    ) {
        this.builder = builder;
        this.method = method;
        this.classLoader = classLoader;
    }

    public Builder<?> annotate() {
        Parameters parameters = new ParametersBuilder(method, classLoader).build();
        ApiResponses apiResponses = new ApiResponsesBuilder(method, classLoader).build();
        SecurityRequirements securityRequirements =
                new SecurityRequirementsBuilder(method, classLoader).build();

        return addAnnotations(builder, method, parameters, apiResponses, securityRequirements);
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            MethodDescription.InDefinedShape method,
            Annotation... annotations
    ) {
        MemberAttributeExtension.ForMethod forMethod = new MemberAttributeExtension.ForMethod();
        for (Annotation annotation : annotations) {
            if (annotation != null) forMethod = forMethod.annotateMethod(annotation);
        }
        return builder.visit(forMethod.on(ElementMatchers.named(method.getName())));
    }
}
