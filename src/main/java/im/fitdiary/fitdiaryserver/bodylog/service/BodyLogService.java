package im.fitdiary.fitdiaryserver.bodylog.service;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import org.springframework.data.domain.Pageable;

public interface BodyLogService {
    BodyLog create(Long userId, CreateBodyLog createBodyLog) throws UserNotFoundException, PreviousHeightNotFound;
    BodyLogSlice searchLatest(Long userId, Pageable pageable);
}
