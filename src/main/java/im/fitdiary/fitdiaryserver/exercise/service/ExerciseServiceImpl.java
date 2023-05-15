package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.ExerciseRepository;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseEditor;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise create(CreateExercise createExercise) {
        Exercise exercise = createExercise.toEntity();
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Transactional(readOnly = true)
    public Exercise findById(Long exerciseId, Long userId) throws ExerciseNotFoundException {
        return exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(ExerciseNotFoundException::new);
    }

    @Transactional
    public void updateById(Long exerciseId, Long userId, ExerciseEditor editor)
            throws ExerciseNotFoundException {
        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(ExerciseNotFoundException::new);
        editor.edit(exercise);
    }

    @Transactional
    public void deleteById(Long exerciseId, Long userId) {
        exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .ifPresent(exerciseRepository::delete);
    }
}
