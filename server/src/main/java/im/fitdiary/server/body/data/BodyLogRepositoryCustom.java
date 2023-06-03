package im.fitdiary.server.body.data;

import im.fitdiary.server.body.data.entity.BodyLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface BodyLogRepositoryCustom {

    Optional<BodyLog> findLatestOne(Long userId);

    Slice<BodyLog> findRecent(Pageable pageable, Long userId);

    void deleteByUserId(Long userId);
}
