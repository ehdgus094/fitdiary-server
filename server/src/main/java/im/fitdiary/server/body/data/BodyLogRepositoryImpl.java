package im.fitdiary.server.body.data;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.querydsl.QuerydslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static im.fitdiary.server.body.data.entity.QBodyLog.*;

@BaseMethodLogging
@RequiredArgsConstructor
public class BodyLogRepositoryImpl implements BodyLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    public Optional<BodyLog> findLatestOne(Long userId) {
        BodyLog foundBodyLog = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.userId.eq(userId))
                .orderBy(bodyLog.measuredAt.desc())
                .fetchFirst();
        return Optional.ofNullable(foundBodyLog);
    }

    public Slice<BodyLog> find(Pageable pageable, Long userId) {
        OrderSpecifier<?>[] orderSpecifiers =
                QuerydslUtil.getOrderSpecifiers(bodyLog, pageable);

        List<BodyLog> content = queryFactory
                .select(bodyLog)
                .from(bodyLog)
                .where(bodyLog.userId.eq(userId))
                .orderBy(orderSpecifiers)
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

    public void deleteByUserId(Long userId) {
        queryFactory
                .update(bodyLog)
                .set(bodyLog.deletedAt, LocalDateTime.now())
                .where(bodyLog.userId.eq(userId))
                .execute();
        em.flush();
        em.clear();
    }
}
