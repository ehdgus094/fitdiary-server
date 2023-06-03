package im.fitdiary.server.exercise.data;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

@BaseMethodLogging
public interface ExerciseLogDetailRepository extends
        JpaRepository<ExerciseLogDetail, Long>, ExerciseLogDetailRepositoryCustom {
}
