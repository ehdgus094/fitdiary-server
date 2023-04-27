package im.fitdiary.fitdiaryserver.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, Object> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = false;

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (value == null && this.annotation.nullable()) {
            result = true;
        }
        if (enumValues != null && value != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())
                    || (this.annotation.ignoreCase() && value.toString().equalsIgnoreCase(enumValue.toString()))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
