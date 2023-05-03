package im.fitdiary.fitdiaryserver.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ValidationTemplate<T> {

    private final T dto;
    private final Validator validator;

    public ValidationTemplate(T dto) {
        this.dto = dto;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public void success() {
        test(true, null, null);
    }

    public void fail(Object failValue, Consumer<T> action) {
        test(false, failValue, action);
    }

    private void test(boolean success, Object failValue, Consumer<T> action) {
        // given
        if (action != null) {
            action.accept(dto);
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
