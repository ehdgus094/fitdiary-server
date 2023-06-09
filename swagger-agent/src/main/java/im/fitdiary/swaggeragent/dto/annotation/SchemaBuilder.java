package im.fitdiary.swaggeragent.dto.annotation;

import im.fitdiary.swaggeragent.logger.Logger;
import io.swagger.v3.oas.annotations.media.Schema;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.nullability.MaybeNull;

import java.math.BigDecimal;
import java.util.Arrays;

public class SchemaBuilder {

    private final Logger logger = new Logger(getClass());
    
    private final TypeDescription jsonNullableType;

    private final TypeDescription enumType;

    private final FieldDescription.InDefinedShape field;

    private AnnotationDescription.Builder builder;

    private final ClassLoader classLoader;

    private boolean annotationRequired = false;

    public SchemaBuilder(FieldDescription.InDefinedShape field, ClassLoader classLoader) {
        TypePool typePool = TypePool.Default.of(classLoader);

        jsonNullableType =
                typePool.describe("org.openapitools.jackson.nullable.JsonNullable").resolve();
        enumType =
                typePool.describe("im.fitdiary.server.common.validation.annotation.Enum").resolve();
        this.field = field;
        builder = AnnotationDescription.Builder.ofType(Schema.class);
        this.classLoader = classLoader;
    }

    @MaybeNull
    public Schema build() {
        addPropertiesForJsonNullable();
        addPropertiesForEnum();

        return annotationRequired
                ? builder.build().prepare(Schema.class).load()
                : null;
    }

    private void addPropertiesForEnum() {
        for (AnnotationDescription annotation : field.getDeclaredAnnotations()) {
            if (annotation.getAnnotationType().equals(enumType)) {
                try {
                    TypeDescription enumDescription =
                            (TypeDescription) annotation.getValue("enumClass").resolve();
                    Class<?> enumClass = Class.forName(enumDescription.getName(), true, classLoader);

                    String[] enumValues = Arrays.stream(enumClass.getEnumConstants())
                            .map(Object::toString)
                            .toArray(String[]::new);
                    annotationRequired = true;
                    builder = builder.defineArray("allowableValues", enumValues);
                } catch (Exception e) {
                    logger.log(e.toString());
                }
            }
        }
    }

    private void addPropertiesForJsonNullable() {
        if (field.getType().asErasure().equals(jsonNullableType)) {
            TypeList.Generic generics = field.getType().getTypeArguments();
            if (!generics.isEmpty() && !isWildCard(generics.get(0))) {
                annotationRequired = true;
                builder = builder
                        .define("requiredMode", Schema.RequiredMode.NOT_REQUIRED)
                        .define("type", getFieldType(generics.get(0)));
            }
        }
    }

    private String getFieldType(TypeDescription.Generic generic) {
        if (generic.asErasure().getSimpleName().equals(BigDecimal.class.getSimpleName())) {
            return "number";
        }
        return generic.asErasure().getSimpleName();
    }

    private boolean isWildCard(TypeDescription.Generic generic) {
        return generic.getSort().equals(TypeDefinition.Sort.WILDCARD);
    }
}
