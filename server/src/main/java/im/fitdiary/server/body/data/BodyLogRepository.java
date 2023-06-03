package im.fitdiary.server.body.data;

import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@BaseMethodLogging
public interface BodyLogRepository extends JpaRepository<BodyLog, Long>, BodyLogRepositoryCustom {

    Optional<BodyLog> findByIdAndUserId(Long exerciseId, Long userId);
}
