package im.fitdiary.fitdiaryserver.bodylog.data;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface BodyLogRepositoryCustom {
    Optional<BodyLog> findLatestOne(User user);
    Slice<BodyLog> searchLatest(Pageable pageable, Long userId);
    Optional<BodyLog> findMineById(Long userId, Long bodyLogId);
}
