package im.fitdiary.server.exercise.service;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.ExerciseRepository;
import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.data.dto.ExerciseEditor;
import im.fitdiary.server.exercise.service.dto.CreateExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@BaseMethodLogging
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

    @Transactional(readOnly = true)
    public long countByIdIn(List<Long> exerciseIds, Long userId) {
        return exerciseRepository.countByIdInAndUserId(exerciseIds, userId);
    }

    @Transactional
    public void updateById(Long exerciseId, Long userId, ExerciseEditor editor)
            throws ExerciseNotFoundException {
        Exercise exercise = findById(exerciseId, userId);
        exercise.update(editor);
    }

    @Transactional
    public void deleteById(Long exerciseId, Long userId) {
        exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .ifPresent(exerciseRepository::delete);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        exerciseRepository.deleteByUserId(userId);
    }
}
