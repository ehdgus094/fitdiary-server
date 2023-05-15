package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.P6SpySqlFormatConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.util.factory.exercise.ExerciseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@DataJpaTest(showSql = false)
@Import({AuditingConfig.class, QuerydslConfig.class, P6SpySqlFormatConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExerciseLogRepositoryTest {

    @Autowired
    ExerciseLogRepository exerciseLogRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("create")
    void create() {
        // given
        ExerciseLog exerciseLog = ExerciseFactory.exerciseLog();
        exerciseLogRepository.save(exerciseLog);
        em.flush();
        em.clear();

        // when
        Optional<ExerciseLog> foundExerciseLog = exerciseLogRepository.findById(exerciseLog.getId());

        // then
        assertThat(foundExerciseLog).isPresent();
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        ExerciseLog exerciseLog = ExerciseFactory.exerciseLog();
        exerciseLogRepository.save(exerciseLog);
        em.flush();
        em.clear();

        // when
        exerciseLogRepository.delete(exerciseLog);
        ExerciseLog foundExerciseLog = (ExerciseLog) em.createNativeQuery(
                "SELECT * FROM exercise_log WHERE id = :id",
                        ExerciseLog.class
                )
                .setParameter("id", exerciseLog.getId())
                .getSingleResult();

        // then
        LocalDateTime deletedAt = (LocalDateTime) getField(foundExerciseLog, "deletedAt");
        assertThat(deletedAt).isNotNull();
    }
}