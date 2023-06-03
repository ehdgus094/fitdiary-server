package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CreateExerciseLogDetailListReqTest {

    ValidationTemplate<CreateExerciseLogDetailListReq> template =
            new ValidationTemplate<>(ExerciseFactory.createExerciseLogDetailListReq());

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

    @Test
    @DisplayName("유효성 검사 Data")
    void validateData() {
        List<String> list = new ArrayList<>();
        template.failure("data", null);
        template.failure("data", list);
        for (int i = 0; i<101; i++) {
            list.add("a");
        }
        template.failure("data", list);
    }
}