package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.ExerciseLogDetailRepository;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExerciseLogDetailServiceImpl implements ExerciseLogDetailService {

    private final ExerciseLogDetailRepository exerciseLogDetailRepository;

    private final ExerciseLogService exerciseLogService;

    private final ExerciseService exerciseService;

    @Transactional
    public void create(
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

        exerciseLogDetailRepository.saveBulk(exerciseLog, createExerciseLogDetails);
    }
}
