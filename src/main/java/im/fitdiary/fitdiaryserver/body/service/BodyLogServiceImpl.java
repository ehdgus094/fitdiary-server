package im.fitdiary.fitdiaryserver.body.service;

import im.fitdiary.fitdiaryserver.body.data.BodyLogRepository;
import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BodyLogServiceImpl implements BodyLogService {

    private final BodyLogRepository bodyLogRepository;

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
    public BodyLogSlice searchLatest(Pageable pageable, Long userId) {
        Slice<BodyLog> bodyLogs = bodyLogRepository.searchLatest(pageable, userId);
        return new BodyLogSlice(bodyLogs.getContent(), bodyLogs.hasNext());
    }

    @Transactional
    public void deleteById(Long bodyLogId, Long userId) {
        bodyLogRepository.findById(bodyLogId, userId)
                .ifPresent(bodyLogRepository::delete);
    }
}