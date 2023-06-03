package im.fitdiary.server.util.template;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
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

    public void success(String fieldName, Object newValue) {
        test(true, fieldName, newValue);
    }

    public void failure(String fieldName, Object newValue) {
        test(false, fieldName, newValue);
    }

    private void test(boolean success, String fieldName, Object newValue) {
        // given
        if (fieldName != null) {
            setField(dto, fieldName, newValue);
        }

        // when
        Set<ConstraintViolation<T>> validate = validator.validate(dto);
        List<String> result = validate.stream()
                .map(tConstraintViolation ->
                        tConstraintViolation.getPropertyPath().toString()
                )
                .collect(Collectors.toList());

        // then
        if (success) {
            assertThat(validate).isEmpty();
        } else {
            assertThat(validate).isNotEmpty();
            assertThat(result).contains(fieldName);
        }
    }
}
