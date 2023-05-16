package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseLogDetailRepository extends
        JpaRepository<ExerciseLogDetail, Long>, ExerciseLogDetailRepositoryCustom {
}
