package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.exercise.ExerciseFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateExerciseLogReqTest {

    ValidationTemplate<CreateExerciseLogReq> template =
            new ValidationTemplate<>(ExerciseFactory.createExerciseLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 Duration")
    void validateDuration() {
        template.fail("duration", null);
        template.fail("duration", 0);
        template.fail("duration", 86401);
    }
}