package im.fitdiary.server.exercise.data;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exercise.data.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@BaseMethodLogging
public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {

    Optional<Exercise> findByIdAndUserId(Long exerciseId, Long userId);

    long countByIdInAndUserId(List<Long> exerciseIds, Long userId);
}
