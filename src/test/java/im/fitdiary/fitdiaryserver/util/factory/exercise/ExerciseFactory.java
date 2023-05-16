package im.fitdiary.fitdiaryserver.util.factory.exercise;

import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.*;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExercise;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExerciseLog;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class ExerciseFactory {

    private static final long USER_ID = 1L;

    private static final String NAME = "test";

    private static final ExerciseCategory CATEGORY = ExerciseCategory.CHEST;

    private static final boolean ACTIVE = true;

    private static final int DURATION = 7200;

    private static final boolean WARM_UP = false;

    private static final int INTERVALS = 60;

    private static final BigDecimal WEIGHT = new BigDecimal("100");

    private static final int COUNT = 10;

    private static final int SUPPORT_COUNT = 1;

    private static final long EXERCISE_LOG_ID = 1L;

    private static final long EXERCISE_ID = 1L;

    public static Exercise exercise() {
        return Exercise.create(
                USER_ID,
                NAME,
                CATEGORY,
                ACTIVE
        );
    }

    public static ExerciseLog exerciseLog() {
        return ExerciseLog.create(
                USER_ID,
                DURATION
        );
    }

    public static ExerciseLogDetail exerciseLogDetail(
            Exercise exercise,
            ExerciseLog exerciseLog,
            int sequence
    ) {
        return ExerciseLogDetail.create(
                exercise,
                exerciseLog,
                sequence,
                WARM_UP,
                INTERVALS,
                WEIGHT,
                COUNT,
                SUPPORT_COUNT
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

    public static CreateExerciseLog createExerciseLog() {
        return new CreateExerciseLog(USER_ID, DURATION);
    }

    public static CreateExerciseLogDetail createExerciseLogDetail(Long exerciseId) {
        return new CreateExerciseLogDetail(
                exerciseId,
                WARM_UP,
                INTERVALS,
                WEIGHT,
                COUNT,
                SUPPORT_COUNT
        );
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

    public static CreateExerciseLogReq createExerciseLogReq() {
        CreateExerciseLogReq req = new CreateExerciseLogReq();
        setField(req, "duration", DURATION);
        return req;
    }

    public static CreateExerciseLogDetailListReq createExerciseLogDetailListReq() {
        CreateExerciseLogDetailListReq req = new CreateExerciseLogDetailListReq();
        setField(req, "exerciseLogId", EXERCISE_LOG_ID);
        List<CreateExerciseLogDetailReq> data = new ArrayList<>();
        data.add(createExerciseLogDetailReq());
        setField(req, "data", data);
        return req;
    }

    public static CreateExerciseLogDetailReq createExerciseLogDetailReq() {
        CreateExerciseLogDetailReq req = new CreateExerciseLogDetailReq();
        setField(req, "exerciseId", EXERCISE_ID);
        setField(req, "warmUp", WARM_UP);
        setField(req, "intervals", INTERVALS);
        setField(req, "weight", WEIGHT);
        setField(req, "count", COUNT);
        setField(req, "supportCount", SUPPORT_COUNT);
        return req;
    }
}
