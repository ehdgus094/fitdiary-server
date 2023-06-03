package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CreateExerciseLogDetailReqTest {

    ValidationTemplate<CreateExerciseLogDetailReq> template =
            new ValidationTemplate<>(ExerciseFactory.createExerciseLogDetailReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 ExerciseId")
    void validateExerciseId() {
        template.failure("exerciseId", null);
        template.failure("exerciseId", 0L);
    }

    @Test
    @DisplayName("유효성 검사 WarmUp")
    void validateWarmUp() {
        template.failure("warmUp", null);
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.failure("weight", null);
        template.failure("weight", new BigDecimal("0"));
        template.failure("weight", new BigDecimal("1000"));
    }

    @Test
    @DisplayName("유효성 검사 Count")
    void validateCount() {
        template.failure("count", null);
        template.failure("count", 0);
        template.failure("count", 10001);
    }

    @Test
    @DisplayName("유효성 검사 SupportCount")
    void validateSupportCount() {
        template.failure("supportCount", null);
        template.failure("supportCount", -1);
        template.failure("supportCount", 10001);
    }
}