package im.fitdiary.server.util.factory.exercise;

import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import im.fitdiary.server.exercise.data.dto.ExerciseEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.presentation.dto.*;
import im.fitdiary.server.exercise.service.dto.CreateExercise;
import im.fitdiary.server.exercise.service.dto.CreateExerciseLog;
import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class ExerciseFactory {

    private static final long USER_ID = 1L;

    private static final String NAME = "test";

    private static final ExerciseCategory CATEGORY = ExerciseCategory.CHEST;

    private static final boolean ACTIVE = true;

    private static final int DURATION = 7200;

    private static final LocalDateTime MEASURED_AT = LocalDateTime.now();

    private static final Long MEASURED_AT_TIMESTAMP = 1683707949L;

    private static final boolean WARM_UP = false;

    private static final BigDecimal WEIGHT = new BigDecimal("100");

    private static final int COUNT = 10;

    private static final int SUPPORT_COUNT = 1;

    private static final long EXERCISE_LOG_ID = 1L;

    private static final long EXERCISE_ID = 1L;

    private static final int SEQUENCE = 0;

    private static final long EXERCISE_LOG_DETAIL_ID = 1L;

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
                DURATION,
                MEASURED_AT
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

    public static ExerciseLogDetailEditor exerciseLogDetailEditor() {
        return new ExerciseLogDetailEditor(
                JsonNullable.of(SEQUENCE),
                JsonNullable.of(WARM_UP),
                JsonNullable.of(WEIGHT),
                JsonNullable.of(COUNT),
                JsonNullable.of(SUPPORT_COUNT)
        );
    }

    public static CreateExercise createExercise() {
        return new CreateExercise(USER_ID, NAME, CATEGORY, ACTIVE);
    }

    public static CreateExerciseLog createExerciseLog() {
        return new CreateExerciseLog(USER_ID, DURATION, MEASURED_AT);
    }

    public static CreateExerciseLogDetail createExerciseLogDetail(Long exerciseId) {
        return new CreateExerciseLogDetail(
                exerciseId,
                WARM_UP,
                WEIGHT,
                COUNT,
                SUPPORT_COUNT
        );
    }

    public static ExerciseLogDetailSlice exerciseLogDetailSlice() {
        List<ExerciseLogDetail> exerciseLogDetails = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Exercise exercise = exercise();
            ExerciseLog exerciseLog = exerciseLog();
            exerciseLogDetails.add(exerciseLogDetail(exercise, exerciseLog, i));
        }
        return new ExerciseLogDetailSlice(exerciseLogDetails, false);
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
        setField(req, "measuredAt", MEASURED_AT_TIMESTAMP);
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
        setField(req, "weight", WEIGHT);
        setField(req, "count", COUNT);
        setField(req, "supportCount", SUPPORT_COUNT);
        return req;
    }

    public static UpdateExerciseLogDetailListReq updateExerciseLogDetailListReq() {
        UpdateExerciseLogDetailListReq req = new UpdateExerciseLogDetailListReq();
        setField(req, "exerciseLogId", EXERCISE_LOG_ID);
        List<UpdateExerciseLogDetailReq> data = new ArrayList<>();
        data.add(updateExerciseLogDetailReq());
        setField(req, "data", data);
        return req;
    }

    public static UpdateExerciseLogDetailReq updateExerciseLogDetailReq() {
        UpdateExerciseLogDetailReq req = new UpdateExerciseLogDetailReq();
        setField(req, "exerciseLogDetailId", EXERCISE_LOG_DETAIL_ID);
        setField(req, "sequence", JsonNullable.of(SEQUENCE));
        setField(req, "warmUp", JsonNullable.of(WARM_UP));
        setField(req, "weight", JsonNullable.of(WEIGHT));
        setField(req, "count", JsonNullable.of(COUNT));
        setField(req, "supportCount", JsonNullable.of(SUPPORT_COUNT));
        return req;
    }
}
