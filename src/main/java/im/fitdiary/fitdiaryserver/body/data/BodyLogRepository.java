package im.fitdiary.fitdiaryserver.body.data;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodyLogRepository extends JpaRepository<BodyLog, Long>, BodyLogRepositoryCustom {

    Optional<BodyLog> findByIdAndUserId(Long exerciseId, Long userId);
}
