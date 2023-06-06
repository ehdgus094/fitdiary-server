package im.fitdiary.server.exercise.service;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.ExerciseLogDetailRepository;
import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public void createBulk(
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

    @Transactional(readOnly = true)
    public ExerciseLogDetailSlice find(Pageable pageable, Long exerciseLogId, Long userId)
            throws ExerciseLogNotFoundException {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, userId);
        Slice<ExerciseLogDetail> exerciseLogDetails =
                exerciseLogDetailRepository.find(pageable, exerciseLog);
        return new ExerciseLogDetailSlice(exerciseLogDetails.getContent(), exerciseLogDetails.hasNext());
    }

    @Transactional
    public void updateBulk(
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
        } catch (ExerciseLogDetailNotFoundException ignored) {
        }
    }
}
