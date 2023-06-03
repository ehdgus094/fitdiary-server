package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

class UpdateExerciseReqTest {

    ValidationTemplate<UpdateExerciseReq> template =
            new ValidationTemplate<>(ExerciseFactory.updateExerciseReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.success("name", JsonNullable.undefined());
        template.failure("name", JsonNullable.of(null));
        template.failure("name", JsonNullable.of(""));
    }

    @Test
    @DisplayName("유효성 검사 Category")
    void validateCategory() {
        template.success("category", JsonNullable.undefined());
        template.failure("category", JsonNullable.of(null));
    }

    @Test
    @DisplayName("유효성 검사 Active")
    void validateActive() {
        template.success("active", JsonNullable.undefined());
        template.failure("active", JsonNullable.of(null));
    }
}