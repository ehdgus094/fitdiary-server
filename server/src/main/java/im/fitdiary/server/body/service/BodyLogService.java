package im.fitdiary.server.body.service;

import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.body.data.dto.BodyLogEditor;
import im.fitdiary.server.body.service.dto.BodyLogSlice;
import im.fitdiary.server.body.service.dto.CreateBodyLog;
import im.fitdiary.server.exception.e404.BodyLogNotFoundException;
import im.fitdiary.server.exception.e404.PreviousHeightNotFound;
import org.springframework.data.domain.Pageable;

public interface BodyLogService {

    BodyLog create(CreateBodyLog createBodyLog) throws PreviousHeightNotFound;

    BodyLog findById(Long bodyLogId, Long userId) throws BodyLogNotFoundException;

    BodyLogSlice findRecent(Pageable pageable, Long userId);

    void updateById(Long bodyLogId, Long userId, BodyLogEditor editor) throws BodyLogNotFoundException;

    void deleteById(Long bodyLogId, Long userId);

    void deleteByUserId(Long userId);
}
