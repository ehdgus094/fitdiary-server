package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;

import java.util.List;
import java.util.Map;

public interface ExerciseLogDetailService {

    void bulkInsert(Long exerciseLogId, Long userId, List<CreateExerciseLogDetail> createExerciseLogDetails)
        throws ExerciseLogNotFoundException, ExerciseNotFoundException;

    ExerciseLogDetail findById(Long exerciseLogDetailId, Long userId)
            throws ExerciseLogDetailNotFoundException;

    void bulkUpdate(Long exerciseLogId, Long userId, Map<Long, ExerciseLogDetailEditor> editors)
        throws ExerciseLogNotFoundException;

    void deleteById(Long exerciseLogDetailId, Long userId);
}
