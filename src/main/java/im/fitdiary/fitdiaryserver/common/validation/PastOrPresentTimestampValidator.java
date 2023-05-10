package im.fitdiary.fitdiaryserver.common.validation;

import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class PastOrPresentTimestampValidator implements ConstraintValidator<PastOrPresentTimestamp, Long> {

    @Override
    public void initialize(PastOrPresentTimestamp constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return true;

        LocalDateTime converted = TimeConverter.toLocalDateTime(value);
        return !converted.isAfter(LocalDateTime.now());
    }
}
