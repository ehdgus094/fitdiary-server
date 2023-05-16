package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;

import java.util.List;

public interface ExerciseLogDetailRepositoryCustom {

    void saveBulk(ExerciseLog exerciseLog, List<CreateExerciseLogDetail> exerciseLogDetails);
}
