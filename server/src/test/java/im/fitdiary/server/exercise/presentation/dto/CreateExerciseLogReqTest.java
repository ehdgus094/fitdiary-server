package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateExerciseLogReqTest {

    ValidationTemplate<CreateExerciseLogReq> template =
            new ValidationTemplate<>(ExerciseFactory.createExerciseLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Duration")
    void validateDuration() {
        template.failure("duration", null);
        template.failure("duration", 0);
        template.failure("duration", 86401);
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.success("measuredAt", null);
        template.failure("measuredAt", 10000000000L);
    }
}