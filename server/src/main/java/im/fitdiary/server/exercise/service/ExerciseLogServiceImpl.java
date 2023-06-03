package im.fitdiary.server.exercise.service;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.ExerciseLogDetailRepository;
import im.fitdiary.server.exercise.data.ExerciseLogRepository;
import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.service.dto.CreateExerciseLog;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class ExerciseLogServiceImpl implements ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;

    private final ExerciseLogDetailRepository exerciseLogDetailRepository;

    private final ExerciseService exerciseService;

    @Timed("custom.log.exercise")
    @Transactional
    public ExerciseLog create(CreateExerciseLog createExerciseLog) {
        ExerciseLog exerciseLog = createExerciseLog.toEntity();
        exerciseLogRepository.save(exerciseLog);
        return exerciseLog;
    }

    @Transactional
    public void bulkInsertDetail(
            Long exerciseLogId,
            Long userId,
            List<CreateExerciseLogDetail> createExerciseLogDetails
    ) throws ExerciseLogNotFoundException, ExerciseNotFoundException {
        ExerciseLog exerciseLog = findById(exerciseLogId, userId);

        Set<Long> exerciseIdSet = createExerciseLogDetails.stream()
                .map(CreateExerciseLogDetail::getExerciseId)
                .collect(Collectors.toSet());
        long count = exerciseService.countByIdIn(new ArrayList<>(exerciseIdSet), userId);
        if (count != exerciseIdSet.size()) throw new ExerciseNotFoundException();

        exerciseLogDetailRepository.bulkInsert(exerciseLog, createExerciseLogDetails);
    }

    @Transactional(readOnly = true)
    public ExerciseLog findById(Long exerciseLogId, Long userId) throws ExerciseLogNotFoundException {
        return exerciseLogRepository.findByIdAndUserId(exerciseLogId, userId)
                .orElseThrow(ExerciseLogNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public ExerciseLogDetail findDetailById(Long exerciseLogDetailId, Long userId)
            throws ExerciseLogDetailNotFoundException {
        ExerciseLogDetail detail = exerciseLogDetailRepository.findById(exerciseLogDetailId)
                .orElseThrow(ExerciseLogDetailNotFoundException::new);
        if (!detail.getExerciseLog().getUserId().equals(userId)) {
            throw new ExerciseLogDetailNotFoundException();
        }
        return detail;
    }

    @Transactional
    public void bulkUpdateDetail(
            Long exerciseLogId,
            Long userId,
            Map<Long, ExerciseLogDetailEditor> editors
    ) throws ExerciseLogNotFoundException {
        ExerciseLog exerciseLog = findById(exerciseLogId, userId);
        exerciseLogDetailRepository.bulkUpdate(exerciseLog, editors);
    }

    @Transactional
    public void deleteById(Long exerciseLogId, Long userId) {
        exerciseLogRepository.findByIdAndUserId(exerciseLogId, userId).ifPresent(exerciseLog -> {
            exerciseLogDetailRepository.deleteByExerciseLog(exerciseLog);
            exerciseLogRepository.delete(exerciseLog);
        });
    }

    @Transactional
    public void deleteDetailById(Long exerciseLogDetailId, Long userId) {
        try {
            ExerciseLogDetail detail = findDetailById(exerciseLogDetailId, userId);
            exerciseLogDetailRepository.deleteSequence(detail);
            exerciseLogDetailRepository.delete(detail);
        } catch (ExerciseLogDetailNotFoundException ignored) {}
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        exerciseLogRepository.deleteWithDetailsByUserId(userId);
    }
}
