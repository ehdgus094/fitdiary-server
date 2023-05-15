package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.exercise.ExerciseFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

class UpdateExerciseReqTest {

    private final ValidationTemplate<UpdateExerciseReq> template =
            new ValidationTemplate<>(ExerciseFactory.updateExerciseReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.succeed("name", JsonNullable.undefined());
        template.fail("name", JsonNullable.of(null));
        template.fail("name", JsonNullable.of(""));
    }

    @Test
    @DisplayName("유효성 검사 Category")
    void validateCategory() {
        template.succeed("category", JsonNullable.undefined());
        template.fail("category", JsonNullable.of(null));
    }

    @Test
    @DisplayName("유효성 검사 Active")
    void validateActive() {
        template.succeed("active", JsonNullable.undefined());
        template.fail("active", JsonNullable.of(null));
    }
}