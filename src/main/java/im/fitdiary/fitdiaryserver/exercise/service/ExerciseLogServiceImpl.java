package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.ExerciseLogRepository;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExerciseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExerciseLogServiceImpl implements ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;

    @Transactional
    public ExerciseLog create(CreateExerciseLog createExerciseLog) {
        ExerciseLog exerciseLog = createExerciseLog.toEntity();
        exerciseLogRepository.save(exerciseLog);
        return exerciseLog;
    }

    @Transactional(readOnly = true)
    public ExerciseLog findById(Long exerciseLogId, Long userId) throws ExerciseLogNotFoundException {
        return exerciseLogRepository.findByIdAndUserId(exerciseLogId, userId)
                .orElseThrow(ExerciseLogNotFoundException::new);
    }

    @Transactional
    public void deleteById(Long exerciseLogId, Long userId) {
        exerciseLogRepository.findByIdAndUserId(exerciseLogId, userId)
                .ifPresent(exerciseLogRepository::delete);
    }
}
