package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.exercise.ExerciseFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateExerciseReqTest {

    ValidationTemplate<CreateExerciseReq> template =
            new ValidationTemplate<>(ExerciseFactory.createExerciseReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.failure("name", null);
        template.failure("name", "");
    }

    @Test
    @DisplayName("유효성 검사 Category")
    void validateCategory() {
        template.failure("category", null);
    }

    @Test
    @DisplayName("유효성 검사 Active")
    void validateActive() {
        template.success("active", null);
    }
}