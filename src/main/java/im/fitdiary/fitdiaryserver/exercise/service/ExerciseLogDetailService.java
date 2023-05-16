package im.fitdiary.fitdiaryserver.exercise.service;

import im.fitdiary.fitdiaryserver.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.ExerciseNotFoundException;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;

import java.util.List;

public interface ExerciseLogDetailService {

    void create(Long exerciseLogId, Long userId, List<CreateExerciseLogDetail> createExerciseLogDetails)
        throws ExerciseLogNotFoundException, ExerciseNotFoundException;
}
