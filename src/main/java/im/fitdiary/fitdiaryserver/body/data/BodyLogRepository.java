package im.fitdiary.fitdiaryserver.body.data;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyLogRepository extends JpaRepository<BodyLog, Long>, BodyLogRepositoryCustom {
}
