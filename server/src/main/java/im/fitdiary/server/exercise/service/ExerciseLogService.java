package im.fitdiary.server.exercise.service;

import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.service.dto.CreateExerciseLog;

public interface ExerciseLogService {

    ExerciseLog create(CreateExerciseLog createExerciseLog);

    ExerciseLog findById(Long exerciseLogId, Long userId) throws ExerciseLogNotFoundException;

    void deleteById(Long exerciseLogId, Long userId);

    void deleteByUserId(Long userId);
}
