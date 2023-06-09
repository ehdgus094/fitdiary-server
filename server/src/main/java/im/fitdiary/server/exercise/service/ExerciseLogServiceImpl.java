package im.fitdiary.server.exercise.service;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exercise.data.ExerciseLogRepository;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.service.dto.CreateExerciseLog;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class ExerciseLogServiceImpl implements ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;

    @Timed("custom.log.exercise")
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
                .ifPresent(exerciseLogRepository::deleteWithDetails);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        exerciseLogRepository.deleteWithDetailsByUserId(userId);
    }
}
