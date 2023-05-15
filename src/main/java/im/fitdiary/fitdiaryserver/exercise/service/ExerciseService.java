package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseEditor;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExercise;

public interface ExerciseService {
    Exercise create(CreateExercise createExercise);
    Exercise findById(Long exerciseId, Long userId) throws ExerciseNotFoundException;
    void update(Long exerciseId, Long userId, ExerciseEditor editor) throws ExerciseNotFoundException;
    void deleteById(Long exerciseId, Long userId);
}
