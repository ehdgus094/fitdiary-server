package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    Optional<ExerciseLog> findByIdAndUserId(Long exerciseLogId, Long userId);
}
