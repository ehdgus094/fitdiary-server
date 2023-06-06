package im.fitdiary.server.exercise.service;

import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ExerciseLogDetailService {

    void createBulk(Long exerciseLogId, Long userId, List<CreateExerciseLogDetail> createExerciseLogDetails)
            throws ExerciseLogNotFoundException, ExerciseNotFoundException;

    ExerciseLogDetail findById(Long exerciseLogDetailId, Long userId)
            throws ExerciseLogDetailNotFoundException;

    ExerciseLogDetailSlice find(Pageable pageable, Long exerciseLogId, Long userId)
            throws ExerciseLogNotFoundException;

    void updateBulk(Long exerciseLogId, Long userId, Map<Long, ExerciseLogDetailEditor> editors)
            throws ExerciseLogNotFoundException;

    void deleteById(Long exerciseLogDetailId, Long userId);
}
