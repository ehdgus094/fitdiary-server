package im.fitdiary.server.exercise.data;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@BaseMethodLogging
@RequiredArgsConstructor
public class ExerciseLogRepositoryImpl implements ExerciseLogRepositoryCustom {

    private final EntityManager em;

    public void deleteWithDetailsByUserId(Long userId) {
        em.createNativeQuery(
                "UPDATE exercise_log log " +
                        "JOIN exercise_log_detail detail " +
                        "ON log.id = detail.exercise_log_id " +
                        "SET log.deleted_at = CURRENT_TIMESTAMP, " +
                        "detail.deleted_at = CURRENT_TIMESTAMP " +
                        "WHERE log.user_id = :userId"
        )
                .setParameter("userId", userId)
                .executeUpdate();
        em.flush();
        em.clear();
    }
}
