package im.fitdiary.swaggeragent.controller.annotation;

import im.fitdiary.swaggeragent.logger.Logger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.nullability.MaybeNull;

import java.util.*;

public class ApiResponseBuilder {

    private final Logger logger = new Logger(getClass());

    private final TypeDescription responseStatusType;

    private final MethodDescription.InDefinedShape method;

    private final ClassLoader classLoader;

    public ApiResponseBuilder(MethodDescription.InDefinedShape method, ClassLoader classLoader) {
        TypePool typePool = TypePool.Default.of(classLoader);

        responseStatusType =
                typePool.describe("org.springframework.web.bind.annotation.ResponseStatus").resolve();
        this.method = method;
        this.classLoader = classLoader;
    }

    public ApiResponses build() {
        List<ApiResponse> apiResponseList = new ArrayList<>();

        TypeList.Generic generics = method.getReturnType().getTypeArguments();
        if (!generics.isEmpty()) {
            if (isWildCard(generics.get(0))) {
                apiResponseList.add(createEmptyResponse());
            } else {
                apiResponseList.add(createResponseWithData());
            }
        }

        if (!method.getExceptionTypes().isEmpty()) {
            Map<String, List<ExampleObject>> responses = new HashMap<>();

            for (TypeDescription.Generic exception : method.getExceptionTypes()) {
                getErrorCode(exception).ifPresent(errorCode -> {
                    List<ExampleObject> exampleObjects = responses.get(errorCode);
                    ExampleObject exampleObject = getExampleObject(
                            exception.asErasure().getSimpleName(),
                            false,
                            getErrorMessage(exception)
                    );
                    if (exampleObjects == null) {
                        responses.put(errorCode, new ArrayList<>(List.of(exampleObject)));
                    } else {
                        exampleObjects.add(exampleObject);
                    }
                });
            }

            for (String errorCode : responses.keySet()) {
                apiResponseList.add(createFailureResponse(errorCode, responses.get(errorCode)));
            }
        }

        return AnnotationDescription.Builder
                .ofType(ApiResponses.class)
                .defineAnnotationArray(
                        "value",
                        ApiResponse.class,
                        apiResponseList.toArray(ApiResponse[]::new)
                )
                .build()
                .prepare(ApiResponses.class)
                .load();
    }

    private ApiResponse createFailureResponse(String errorCode, List<ExampleObject> exampleObjects) {
        return AnnotationDescription.Builder
                .ofType(ApiResponse.class)
                .define("responseCode", errorCode)
                .defineAnnotationArray("content", Content.class, AnnotationDescription.Builder
                        .ofType(Content.class)
                        .defineAnnotationArray(
                                "examples",
                                ExampleObject.class,
                                exampleObjects.toArray(ExampleObject[]::new)
                        )
                        .build()
                        .prepare(Content.class)
                        .load()
                )
                .build()
                .prepare(ApiResponse.class)
                .load();
    }

    private ApiResponse createResponseWithData() {
        return AnnotationDescription.Builder
                .ofType(ApiResponse.class)
                .define("responseCode", "200")
                .define("useReturnTypeSchema", true)
                .build()
                .prepare(ApiResponse.class)
                .load();
    }

    private ApiResponse createEmptyResponse() {
        return AnnotationDescription.Builder
                .ofType(ApiResponse.class)
                .define("responseCode", "200")
                .defineAnnotationArray("content", Content.class, AnnotationDescription.Builder
                        .ofType(Content.class)
                        .defineAnnotationArray(
                                "examples",
                                ExampleObject.class,
                                getExampleObject("", true, null)
                        )
                        .build()
                        .prepare(Content.class)
                        .load()
                )
                .build()
                .prepare(ApiResponse.class)
                .load();
    }

    private boolean isWildCard(TypeDescription.Generic generic) {
        return generic.getSort().equals(TypeDefinition.Sort.WILDCARD);
    }

    private @MaybeNull String getErrorMessage(TypeDescription.Generic exception) {
        try {
            Class<?> exceptionClass =
                    Class.forName(exception.getTypeName(), true, classLoader);
            RuntimeException runtimeException =
                    (RuntimeException) exceptionClass.getConstructor().newInstance();
            return runtimeException.getMessage();
        } catch (Exception e) {
            logger.log(e.toString());
        }
        return null;
    }

    private Optional<String> getErrorCode(TypeDescription.Generic exception) {
        for (AnnotationDescription superClassAnnotation :
                Objects.requireNonNull(exception.getSuperClass()).asErasure().getDeclaredAnnotations()) {
            if (superClassAnnotation.getAnnotationType().equals(responseStatusType)) {
                String errorCode = superClassAnnotation
                        .getValue("value")
                        .load(classLoader)
                        .toString()
                        .split(" ")[0];
                return Optional.of(errorCode);
            }
        }
        return Optional.empty();
    }

    private ExampleObject getExampleObject(String name, boolean success, @MaybeNull String message) {
        String messagePart = message == null ? "" : ", \"message\": \"" + message + "\"";
        String value = "{\"success\": " + success + messagePart + "}";
        return AnnotationDescription.Builder
                .ofType(ExampleObject.class)
                .define("name", name)
                .define("value", value)
                .build()
                .prepare(ExampleObject.class)
                .load();
    }
}
