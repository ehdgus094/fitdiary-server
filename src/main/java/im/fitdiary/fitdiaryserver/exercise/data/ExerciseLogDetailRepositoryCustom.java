package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;

import java.util.List;
import java.util.Map;

public interface ExerciseLogDetailRepositoryCustom {

    void bulkInsert(ExerciseLog exerciseLog, List<CreateExerciseLogDetail> exerciseLogDetails);

    void bulkUpdate(ExerciseLog exerciseLog, Map<Long, ExerciseLogDetailEditor> editors);

    void deleteSequence(ExerciseLogDetail detail);
}
