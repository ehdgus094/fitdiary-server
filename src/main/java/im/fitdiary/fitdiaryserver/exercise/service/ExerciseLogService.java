package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExerciseLog;

public interface ExerciseLogService {

    ExerciseLog create(CreateExerciseLog createExerciseLog);

    ExerciseLog findById(Long exerciseLogId, Long userId) throws ExerciseLogNotFoundException;

    void deleteById(Long exerciseLogId, Long userId);
}
