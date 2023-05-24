package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.ExerciseLogDetailRepository;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
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
public class ExerciseLogDetailServiceImpl implements ExerciseLogDetailService {

    private final ExerciseLogDetailRepository exerciseLogDetailRepository;

    private final ExerciseLogService exerciseLogService;

    private final ExerciseService exerciseService;

    @Transactional
    public void bulkInsert(
            Long exerciseLogId,
            Long userId,
            List<CreateExerciseLogDetail> createExerciseLogDetails
    ) throws ExerciseLogNotFoundException, ExerciseNotFoundException {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, userId);

        Set<Long> exerciseIdSet = createExerciseLogDetails.stream()
                .map(CreateExerciseLogDetail::getExerciseId)
                .collect(Collectors.toSet());
        long count = exerciseService.countByIdIn(new ArrayList<>(exerciseIdSet), userId);
        if (count != exerciseIdSet.size()) throw new ExerciseNotFoundException();

        exerciseLogDetailRepository.bulkInsert(exerciseLog, createExerciseLogDetails);
    }

    @Transactional(readOnly = true)
    public ExerciseLogDetail findById(Long exerciseLogDetailId, Long userId)
            throws ExerciseLogDetailNotFoundException {
        ExerciseLogDetail detail = exerciseLogDetailRepository.findById(exerciseLogDetailId)
                .orElseThrow(ExerciseLogDetailNotFoundException::new);
        if (!detail.getExerciseLog().getUserId().equals(userId)) {
            throw new ExerciseLogDetailNotFoundException();
        }
        return detail;
    }

    @Transactional
    public void bulkUpdate(
            Long exerciseLogId,
            Long userId,
            Map<Long, ExerciseLogDetailEditor> editors
    ) throws ExerciseLogNotFoundException {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, userId);
        exerciseLogDetailRepository.bulkUpdate(exerciseLog, editors);
    }

    @Transactional
    public void deleteById(Long exerciseLogDetailId, Long userId) {
        try {
            ExerciseLogDetail detail = findById(exerciseLogDetailId, userId);
            exerciseLogDetailRepository.deleteSequence(detail);
            exerciseLogDetailRepository.delete(detail);
        } catch (ExerciseLogDetailNotFoundException ignored) {}
    }
}
