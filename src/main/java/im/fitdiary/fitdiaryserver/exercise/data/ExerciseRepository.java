package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@BaseMethodLogging
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByIdAndUserId(Long exerciseId, Long userId);

    long countByIdInAndUserId(List<Long> exerciseIds, Long userId);
}
