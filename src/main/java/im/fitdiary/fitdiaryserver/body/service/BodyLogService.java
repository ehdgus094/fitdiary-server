package im.fitdiary.fitdiaryserver.body.service;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import org.springframework.data.domain.Pageable;

public interface BodyLogService {
    BodyLog create(CreateBodyLog createBodyLog) throws PreviousHeightNotFound;
    BodyLogSlice searchLatest(Pageable pageable, Long userId);
    void deleteById(Long bodyLogId, Long userId);
}
