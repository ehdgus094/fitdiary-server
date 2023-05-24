package im.fitdiary.fitdiaryserver.body.data;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@BaseMethodLogging
public interface BodyLogRepository extends JpaRepository<BodyLog, Long>, BodyLogRepositoryCustom {

    Optional<BodyLog> findByIdAndUserId(Long exerciseId, Long userId);
}
