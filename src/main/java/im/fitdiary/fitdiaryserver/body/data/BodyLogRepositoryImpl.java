package im.fitdiary.fitdiaryserver.body.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static im.fitdiary.fitdiaryserver.body.data.entity.QBodyLog.*;

@RequiredArgsConstructor
public class BodyLogRepositoryImpl implements BodyLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<BodyLog> findLatestOne(Long userId) {
        BodyLog foundBodyLog = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.userId.eq(userId))
                .orderBy(bodyLog.measuredAt.desc())
                .fetchFirst();
        return Optional.ofNullable(foundBodyLog);
    }

    public Slice<BodyLog> searchLatest(Pageable pageable, Long userId) {
        List<BodyLog> content = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.userId.eq(userId))
                .orderBy(bodyLog.measuredAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + (long) 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public Optional<BodyLog> findById(Long bodyLogId, Long userId) {
        BodyLog foundBodyLog = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.userId.eq(userId), bodyLog.id.eq(bodyLogId))
                .fetchFirst();
        return Optional.ofNullable(foundBodyLog);
    }
}
