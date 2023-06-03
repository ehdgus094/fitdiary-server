package im.fitdiary.server.exercise.data;

import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;

import java.util.List;
import java.util.Map;

public interface ExerciseLogDetailRepositoryCustom {

    void bulkInsert(ExerciseLog exerciseLog, List<CreateExerciseLogDetail> exerciseLogDetails);

    void bulkUpdate(ExerciseLog exerciseLog, Map<Long, ExerciseLogDetailEditor> editors);

    void deleteSequence(ExerciseLogDetail detail);

    void deleteByExerciseLog(ExerciseLog exerciseLog);
}
