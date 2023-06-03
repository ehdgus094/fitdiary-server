package im.fitdiary.server.exercise.data;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@BaseMethodLogging
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long>, ExerciseLogRepositoryCustom {

    Optional<ExerciseLog> findByIdAndUserId(Long exerciseLogId, Long userId);
}
