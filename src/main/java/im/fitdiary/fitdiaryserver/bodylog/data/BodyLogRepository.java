package im.fitdiary.fitdiaryserver.bodylog.data;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyLogRepository extends JpaRepository<BodyLog, Long>, BodyLogRepositoryCustom {
}
