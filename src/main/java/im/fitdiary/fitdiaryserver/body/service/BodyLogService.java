package im.fitdiary.fitdiaryserver.body.service;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.data.dto.BodyLogEditor;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.exception.e404.BodyLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import org.springframework.data.domain.Pageable;

public interface BodyLogService {

    BodyLog create(CreateBodyLog createBodyLog) throws PreviousHeightNotFound;

    BodyLogSlice searchLatest(Pageable pageable, Long userId);

    void updateById(Long bodyLogId, Long userId, BodyLogEditor editor) throws BodyLogNotFoundException;

    void deleteById(Long bodyLogId, Long userId);
}
