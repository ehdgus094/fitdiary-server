package im.fitdiary.swaggeragent.controller.annotation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.pool.TypePool;

import java.util.Arrays;

public class SecurityRequirementsBuilder {

    private final TypeDescription securedType;

    private final MethodDescription.InDefinedShape method;

    public SecurityRequirementsBuilder(MethodDescription.InDefinedShape method, ClassLoader classLoader) {
        TypePool typePool = TypePool.Default.of(classLoader);

        securedType = typePool.describe("org.springframework.security.access.annotation.Secured").resolve();
        this.method = method;
    }

    public SecurityRequirements build() {
        SecurityRequirement[] securityRequirementList = method.getDeclaredAnnotations().stream()
                .filter(annotation -> annotation.getAnnotationType().equals(securedType))
                .flatMap(annotation -> Arrays.stream((String[]) annotation.getValue("value").resolve()))
                .map(roleType -> AnnotationDescription.Builder
                        .ofType(SecurityRequirement.class)
                        .define("name", roleType)
                        .build()
                        .prepare(SecurityRequirement.class)
                        .load()
                )
                .toArray(SecurityRequirement[]::new);

        return AnnotationDescription.Builder
                .ofType(SecurityRequirements.class)
                .defineAnnotationArray(
                        "value",
                        SecurityRequirement.class,
                        securityRequirementList
                )
                .build()
                .prepare(SecurityRequirements.class)
                .load();
    }
}
