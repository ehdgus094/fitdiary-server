package im.fitdiary.fitdiaryserver.exercise.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static im.fitdiary.fitdiaryserver.exercise.data.entity.QExerciseLog.*;
import static im.fitdiary.fitdiaryserver.exercise.data.entity.QExerciseLogDetail.*;

@BaseMethodLogging
@RequiredArgsConstructor
public class ExerciseLogDetailRepositoryImpl implements ExerciseLogDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    private final JdbcTemplate jdbcTemplate;

    private static final int BULK_INSERT_LIMIT = 100;

    private static final int BULK_UPDATE_LIMIT = 100;

    public void bulkInsert(ExerciseLog exerciseLog, List<CreateExerciseLogDetail> exerciseLogDetails) {
        if (exerciseLogDetails.isEmpty()) return;

        for (int page = 0; page <= exerciseLogDetails.size() / BULK_INSERT_LIMIT; page++) {
            int saved = BULK_INSERT_LIMIT * page;
            int batchSize = exerciseLogDetails.size() >= saved + BULK_INSERT_LIMIT
                    ? BULK_INSERT_LIMIT
                    : exerciseLogDetails.size() - saved;
            jdbcTemplate.batchUpdate(
                    "INSERT INTO exercise_log_detail" +
                            "(exercise_id, " +
                            "exercise_log_id, " +
                            "sequence, " +
                            "warm_up, " +
                            "weight, " +
                            "count, " +
                            "support_count) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            int currIndex = i + saved;
                            ps.setLong(1, exerciseLogDetails.get(currIndex).getExerciseId());
                            ps.setLong(2, exerciseLog.getId());
                            ps.setInt(3, currIndex);
                            ps.setBoolean(4, exerciseLogDetails.get(currIndex).isWarmUp());
                            ps.setBigDecimal(5, exerciseLogDetails.get(currIndex).getWeight());
                            ps.setInt(6, exerciseLogDetails.get(currIndex).getCount());
                            ps.setInt(7, exerciseLogDetails.get(currIndex).getSupportCount());
                        }

                        @Override
                        public int getBatchSize() {
                            return batchSize;
                        }
                    }
            );
        }
    }

    public void bulkUpdate(ExerciseLog log, Map<Long, ExerciseLogDetailEditor> editors) {
        if (editors.isEmpty()) return;

        List<Long> ids = new ArrayList<>(editors.keySet());
        for (int page = 0; page <= ids.size() / BULK_UPDATE_LIMIT; page++) {
            int saved = BULK_UPDATE_LIMIT * page;
            int fetchIdSize = ids.size() >= saved + BULK_UPDATE_LIMIT
                    ? BULK_UPDATE_LIMIT
                    : ids.size() - saved;

            List<ExerciseLogDetail> foundDetails = queryFactory
                    .selectFrom(exerciseLogDetail)
                    .join(exerciseLogDetail.exerciseLog, exerciseLog)
                    .where(
                            exerciseLog.id.eq(log.getId()),
                            exerciseLogDetail.id.in(ids.subList(saved, saved + fetchIdSize))
                    )
                    .fetch();

            jdbcTemplate.batchUpdate(
                    "UPDATE exercise_log_detail " +
                            "SET sequence = ?, " +
                            "warm_up = ?, " +
                            "weight = ?, " +
                            "count = ?, " +
                            "support_count = ? " +
                            "WHERE id = ?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ExerciseLogDetailEditor editor = editors.get(foundDetails.get(i).getId());
                            ps.setInt(1, editor.getSequence().isPresent()
                                    ? editor.getSequence().get()
                                    : foundDetails.get(i).getSequence()
                            );
                            ps.setBoolean(2, editor.getWarmUp().isPresent()
                                    ? editor.getWarmUp().get()
                                    : foundDetails.get(i).isWarmUp()
                            );
                            ps.setBigDecimal(3, editor.getWeight().isPresent()
                                    ? editor.getWeight().get()
                                    : foundDetails.get(i).getWeight()
                            );
                            ps.setInt(4, editor.getCount().isPresent()
                                    ? editor.getCount().get()
                                    : foundDetails.get(i).getCount()
                            );
                            ps.setInt(5, editor.getSupportCount().isPresent()
                                    ? editor.getSupportCount().get()
                                    : foundDetails.get(i).getSupportCount()
                            );
                            ps.setLong(6, foundDetails.get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return foundDetails.size();
                        }
                    }
            );
            em.flush();
            em.clear();
        }
    }

    public void deleteSequence(ExerciseLogDetail detail) {
        queryFactory
                .update(exerciseLogDetail)
                .set(exerciseLogDetail.sequence, exerciseLogDetail.sequence.add(-1))
                .where(exerciseLogDetail.sequence.gt(detail.getSequence()))
                .execute();
        em.flush();
        em.clear();
    }

    public void deleteByExerciseLog(ExerciseLog exerciseLog) {
        queryFactory
                .update(exerciseLogDetail)
                .set(exerciseLogDetail.deletedAt, LocalDateTime.now())
                .where(exerciseLogDetail.exerciseLog.eq(exerciseLog))
                .execute();
        em.flush();
        em.clear();
    }
}
