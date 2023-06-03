package im.fitdiary.server.body.service;

import im.fitdiary.server.body.data.BodyLogRepository;
import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.body.data.dto.BodyLogEditor;
import im.fitdiary.server.body.service.dto.BodyLogSlice;
import im.fitdiary.server.body.service.dto.CreateBodyLog;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.BodyLogNotFoundException;
import im.fitdiary.server.exception.e404.PreviousHeightNotFound;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class BodyLogServiceImpl implements BodyLogService {

    private final BodyLogRepository bodyLogRepository;

    @Timed("custom.log.body")
    @Transactional
    public BodyLog create(CreateBodyLog createBodyLog) throws PreviousHeightNotFound {
        if (createBodyLog.getHeight() == null) {
            BodyLog latestBodyLog = bodyLogRepository.findLatestOne(createBodyLog.getUserId())
                    .orElseThrow(PreviousHeightNotFound::new);
            createBodyLog.updateHeight(latestBodyLog.getHeight());
        }
        BodyLog bodyLog = createBodyLog.toEntity();
        bodyLogRepository.save(bodyLog);
        return bodyLog;
    }

    @Transactional(readOnly = true)
    public BodyLog findById(Long bodyLogId, Long userId) throws BodyLogNotFoundException {
        return bodyLogRepository.findByIdAndUserId(bodyLogId, userId)
                .orElseThrow(BodyLogNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public BodyLogSlice findRecent(Pageable pageable, Long userId) {
        Slice<BodyLog> bodyLogs = bodyLogRepository.findRecent(pageable, userId);
        return new BodyLogSlice(bodyLogs.getContent(), bodyLogs.hasNext());
    }

    @Transactional
    public void updateById(Long bodyLogId, Long userId, BodyLogEditor editor)
            throws BodyLogNotFoundException {
        BodyLog bodyLog = findById(bodyLogId, userId);
        bodyLog.update(editor);
    }

    @Transactional
    public void deleteById(Long bodyLogId, Long userId) {
        bodyLogRepository.findByIdAndUserId(bodyLogId, userId)
                .ifPresent(bodyLogRepository::delete);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        bodyLogRepository.deleteByUserId(userId);
    }
}
