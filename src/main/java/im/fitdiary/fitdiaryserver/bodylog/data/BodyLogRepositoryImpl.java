package im.fitdiary.fitdiaryserver.bodylog.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static im.fitdiary.fitdiaryserver.bodylog.data.entity.QBodyLog.*;
import static im.fitdiary.fitdiaryserver.user.data.entity.QUser.*;

@RequiredArgsConstructor
public class BodyLogRepositoryImpl implements BodyLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<BodyLog> findLatestOne(User user) {
        BodyLog foundBodyLog = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.user.eq(user))
                .orderBy(bodyLog.measuredAt.desc())
                .fetchFirst();
        return Optional.ofNullable(foundBodyLog);
    }

    public Slice<BodyLog> searchLatest(Pageable pageable, Long userId) {
        List<BodyLog> content = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .join(bodyLog.user, user)
                .where(user.id.eq(userId))
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
}
