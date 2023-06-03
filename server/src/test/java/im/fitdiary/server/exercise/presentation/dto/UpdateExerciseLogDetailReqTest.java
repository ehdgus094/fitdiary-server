package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;

class UpdateExerciseLogDetailReqTest {

    ValidationTemplate<UpdateExerciseLogDetailReq> template =
            new ValidationTemplate<>(ExerciseFactory.updateExerciseLogDetailReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 ExerciseLogDetailId")
    void validateExerciseLogDetailId() {
        template.failure("exerciseLogDetailId", null);
        template.failure("exerciseLogDetailId", 0L);
    }

    @Test
    @DisplayName("유효성 검사 Sequence")
    void validateSequence() {
        template.failure("sequence", JsonNullable.of(null));
        template.failure("sequence", JsonNullable.of(-1));
    }

    @Test
    @DisplayName("유효성 검사 WarmUp")
    void validateWarmUp() {
        template.failure("warmUp", JsonNullable.of(null));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.failure("weight", JsonNullable.of(null));
        template.failure("weight", JsonNullable.of(new BigDecimal("0")));
        template.failure("weight", JsonNullable.of(new BigDecimal("1000")));
    }

    @Test
    @DisplayName("유효성 검사 Count")
    void validateCount() {
        template.failure("count", JsonNullable.of(null));
        template.failure("count", JsonNullable.of(0));
        template.failure("count", JsonNullable.of(10001));
    }

    @Test
    @DisplayName("유효성 검사 SupportCount")
    void validateSupportCount() {
        template.failure("supportCount", JsonNullable.of(null));
        template.failure("supportCount", JsonNullable.of(-1));
        template.failure("supportCount", JsonNullable.of(10001));
    }
}