package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdateExerciseLogDetailListReqTest {

    ValidationTemplate<UpdateExerciseLogDetailListReq> template =
            new ValidationTemplate<>(ExerciseFactory.updateExerciseLogDetailListReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 ExerciseLogId")
    void validateExerciseLogId() {
        template.failure("exerciseLogId", null);
        template.failure("exerciseLogId", 0L);
    }
}