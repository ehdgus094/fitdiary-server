package im.fitdiary.fitdiaryserver.exercise.data;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class ExerciseLogDetailRepositoryImpl implements ExerciseLogDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    private final JdbcTemplate jdbcTemplate;

    public void saveBulk(ExerciseLog exerciseLog, List<CreateExerciseLogDetail> exerciseLogDetails) {
        if (exerciseLogDetails.isEmpty()) return;
        int limit = 100;

        for (int page = 0; page <= exerciseLogDetails.size() / limit; page++) {
            int saved = limit * page;
            jdbcTemplate.batchUpdate(
                    "INSERT INTO exercise_log_detail" +
                            "(exercise_id, " +
                            "exercise_log_id, " +
                            "sequence, " +
                            "warm_up, " +
                            "intervals, " +
                            "weight, " +
                            "count, " +
                            "support_count) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            int currIndex = i + saved;
                            ps.setLong(1, exerciseLogDetails.get(currIndex).getExerciseId());
                            ps.setLong(2, exerciseLog.getId());
                            ps.setInt(3, currIndex + 1);
                            ps.setBoolean(4, exerciseLogDetails.get(currIndex).isWarmUp());
                            ps.setInt(5, exerciseLogDetails.get(currIndex).getIntervals());
                            ps.setBigDecimal(6, exerciseLogDetails.get(currIndex).getWeight());
                            ps.setInt(7, exerciseLogDetails.get(currIndex).getCount());
                            ps.setInt(8, exerciseLogDetails.get(currIndex).getSupportCount());
                        }

                        @Override
                        public int getBatchSize() {
                            return exerciseLogDetails.size() >= saved + limit
                                    ? limit
                                    : exerciseLogDetails.size() - saved;
                        }
                    }
            );
        }
    }
}
