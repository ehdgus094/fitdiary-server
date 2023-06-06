package im.fitdiary.server.exercise.data;

import im.fitdiary.server.exercise.data.entity.ExerciseLog;

public interface ExerciseLogRepositoryCustom {

    void deleteWithDetails(ExerciseLog exerciseLog);

    void deleteWithDetailsByUserId(Long userId);
}
