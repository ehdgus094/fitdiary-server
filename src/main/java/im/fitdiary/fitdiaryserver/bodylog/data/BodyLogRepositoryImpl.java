package im.fitdiary.fitdiaryserver.bodylog.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static im.fitdiary.fitdiaryserver.bodylog.data.entity.QBodyLog.*;

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
}
