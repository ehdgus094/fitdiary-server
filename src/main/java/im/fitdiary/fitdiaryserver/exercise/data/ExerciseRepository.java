package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByIdAndUserId(Long exerciseId, Long userId);

    long countByIdInAndUserId(List<Long> exerciseIds, Long userId);
}
