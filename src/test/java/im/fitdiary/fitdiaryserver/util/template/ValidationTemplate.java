package im.fitdiary.fitdiaryserver.util.template;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

public class ValidationTemplate<T> {

    private final T dto;
    private final Validator validator;

    public ValidationTemplate(T dto) {
        this.dto = dto;
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public void success() {
        test(true, null, null);
    }

    public void fail(String fieldName, Object failValue) {
        test(false, fieldName, failValue);
    }

    private void test(boolean success, String fieldName, Object failValue) {
        // given
        if (fieldName != null) {
            setField(dto, fieldName, failValue);
        }

        // when
        Set<ConstraintViolation<T>> validate = validator.validate(dto);
        Set<Object> result = validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet());

        // then
        if (success) {
            assertThat(validate).isEmpty();
        } else {
            assertThat(validate).isNotEmpty();
            assertThat(result).contains(failValue);
        }
    }
}
