package im.fitdiary.fitdiaryserver.bodylog.service;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.bodylog.data.BodyLogRepository;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.exception.e404.BodyLogNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BodyLogServiceImpl implements BodyLogService {

    private final BodyLogRepository bodyLogRepository;
    private final UserService userService;

    @Transactional
    public BodyLog create(Long userId, CreateBodyLog createBodyLog)
            throws UserNotFoundException, PreviousHeightNotFound {
        User user = userService.findById(userId);
        if (createBodyLog.getHeight() == null) {
            BodyLog latestBodyLog = bodyLogRepository.findLatestOne(user)
                    .orElseThrow(PreviousHeightNotFound::new);
            createBodyLog.updateHeight(latestBodyLog.getHeight());
        }
        BodyLog bodyLog = createBodyLog.toEntity(user);
        bodyLogRepository.save(bodyLog);
        return bodyLog;
    }

    @Transactional(readOnly = true)
    public BodyLogSlice searchLatest(Long userId, Pageable pageable) {
        Slice<BodyLog> bodyLogs = bodyLogRepository.searchLatest(pageable, userId);
        return new BodyLogSlice(bodyLogs.getContent(), bodyLogs.hasNext());
    }

    @Transactional
    public void deleteMineById(Long userId, Long bodyLogId) throws BodyLogNotFoundException {
        BodyLog bodyLog = bodyLogRepository.findMineById(userId, bodyLogId)
                .orElseThrow(BodyLogNotFoundException::new);
        bodyLogRepository.delete(bodyLog);
    }
}
