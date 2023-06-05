package im.fitdiary.server.exercise.service;

import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.service.dto.CreateExerciseLog;

import java.util.List;
import java.util.Map;

public interface ExerciseLogService {

    ExerciseLog create(CreateExerciseLog createExerciseLog);

    void bulkInsertDetail(Long exerciseLogId, Long userId, List<CreateExerciseLogDetail> createExerciseLogDetails)
            throws ExerciseLogNotFoundException, ExerciseNotFoundException;

    ExerciseLog findById(Long exerciseLogId, Long userId) throws ExerciseLogNotFoundException;

    ExerciseLogDetail findDetailById(Long exerciseLogDetailId, Long userId) throws ExerciseLogDetailNotFoundException;

    void bulkUpdateDetail(Long exerciseLogId, Long userId, Map<Long, ExerciseLogDetailEditor> editors)
            throws ExerciseLogNotFoundException;

    void deleteById(Long exerciseLogId, Long userId);

    void deleteDetailById(Long exerciseLogDetailId, Long userId);

    void deleteByUserId(Long userId);
}
