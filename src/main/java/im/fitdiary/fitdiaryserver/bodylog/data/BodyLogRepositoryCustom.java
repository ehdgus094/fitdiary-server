package im.fitdiary.fitdiaryserver.bodylog.data;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;

import java.util.Optional;

public interface BodyLogRepositoryCustom {
    Optional<BodyLog> findLatestOne(Long userId);
}
