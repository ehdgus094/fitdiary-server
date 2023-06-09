package im.fitdiary.server.exercise.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static im.fitdiary.server.exercise.data.entity.QExercise.*;

@BaseMethodLogging
@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    public void deleteByUserId(Long userId) {
        queryFactory
                .update(exercise)
                .set(exercise.deletedAt, LocalDateTime.now())
                .where(exercise.userId.eq(userId))
                .execute();
        em.flush();
        em.clear();
    }
}
