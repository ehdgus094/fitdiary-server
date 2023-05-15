package im.fitdiary.fitdiaryserver.util.factory.exercise;

import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseEditor;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.CreateExerciseReq;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.UpdateExerciseReq;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExercise;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class ExerciseFactory {

    private static final Long USER_ID = 1L;

    private static final String NAME = "test";

    private static final ExerciseCategory CATEGORY = ExerciseCategory.CHEST;

    private static final boolean ACTIVE = true;

    public static Exercise exercise() {
        return Exercise.create(
                USER_ID,
                NAME,
                CATEGORY,
                ACTIVE
        );
    }

    public static ExerciseEditor exerciseEditor() {
        return new ExerciseEditor(
                JsonNullable.of(NAME),
                JsonNullable.of(CATEGORY),
                JsonNullable.of(ACTIVE)
        );
    }

    public static CreateExercise createExercise() {
        return new CreateExercise(USER_ID, NAME, CATEGORY, ACTIVE);
    }

    public static CreateExerciseReq createExerciseReq() {
        CreateExerciseReq req = new CreateExerciseReq();
        setField(req, "name", NAME);
        setField(req, "category", CATEGORY.toString());
        setField(req, "active", ACTIVE);
        return req;
    }

    public static UpdateExerciseReq updateExerciseReq() {
        UpdateExerciseReq req = new UpdateExerciseReq();
        setField(req, "name", JsonNullable.of(NAME));
        setField(req, "category", JsonNullable.of(CATEGORY.toString()));
        setField(req, "active", JsonNullable.of(ACTIVE));
        return req;
    }
}
