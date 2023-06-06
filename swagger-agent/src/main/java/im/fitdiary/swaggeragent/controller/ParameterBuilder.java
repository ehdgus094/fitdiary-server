package im.fitdiary.swaggeragent.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.pool.TypePool;

import java.util.ArrayList;
import java.util.List;

public class ParameterBuilder {

    private final TypeDescription authType;

    private final TypeDescription requestHeaderType;

    private final TypeDescription pageableType;

    private final MethodDescription.InDefinedShape method;

    public ParameterBuilder(TypePool typePool, MethodDescription.InDefinedShape method) {
        authType = typePool.describe("im.fitdiary.server.security.argumentresolver.Auth").resolve();
        requestHeaderType = typePool.describe("org.springframework.web.bind.annotation.RequestHeader").resolve();
        pageableType = typePool.describe("org.springframework.data.domain.Pageable").resolve();
        this.method = method;
    }

    public Parameters build() {
        List<Parameter> parameterList = new ArrayList<>();

        for (ParameterDescription.InDefinedShape parameter : method.getParameters()) {
            for (AnnotationDescription annotation : parameter.getDeclaredAnnotations()) {
                if (annotation.getAnnotationType().equals(authType)) {
                    parameterList.add(createAuthParameter(parameter.getName()));
                }

                if (annotation.getAnnotationType().equals(requestHeaderType)) {
                    String header = annotation.getValue("value").resolve(String.class);
                    parameterList.add(createRequestHeaderParameter(header));
                }
            }

            if (parameter.getType().asErasure().equals(pageableType)) {
                parameterList.add(createPageableParameter(parameter.getName()));
            }
        }

        return AnnotationDescription.Builder
                .ofType(Parameters.class)
                .defineAnnotationArray(
                        "value",
                        Parameter.class,
                        parameterList.toArray(Parameter[]::new)
                )
                .build()
                .prepare(Parameters.class)
                .load();
    }

    private Parameter createPageableParameter(String parameterName) {
        return AnnotationDescription.Builder
                .ofType(Parameter.class)
                .define("name", parameterName)
                .defineAnnotationArray(
                        "examples",
                        ExampleObject.class,
                        AnnotationDescription.Builder
                                .ofType(ExampleObject.class)
                                .define("name", "Pageable")
                                .define("value", "{\"page\": 0, \"size\": 1, \"sort\": [\"fieldName,asc\"]}")
                                .build()
                                .prepare(ExampleObject.class)
                                .load()
                )
                .build()
                .prepare(Parameter.class)
                .load();
    }

    private Parameter createRequestHeaderParameter(String header) {
        return AnnotationDescription.Builder
                .ofType(Parameter.class)
                .define("name", header)
                .define("in", ParameterIn.HEADER)
                .define("hidden", true)
                .build()
                .prepare(Parameter.class)
                .load();
    }

    private Parameter createAuthParameter(String parameterName) {
        return AnnotationDescription.Builder
                .ofType(Parameter.class)
                .define("name", parameterName)
                .define("hidden", true)
                .build()
                .prepare(Parameter.class)
                .load();
    }
}
