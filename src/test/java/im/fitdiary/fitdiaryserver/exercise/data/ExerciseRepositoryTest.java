package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.P6SpySqlFormatConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
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
class ExerciseRepositoryTest {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("create")
    void create() {
        // given
        Exercise exercise = ExerciseFactory.exercise();
        exerciseRepository.save(exercise);
        em.flush();
        em.clear();

        // when
        Optional<Exercise> foundExercise = exerciseRepository.findById(exercise.getId());

        // then
        assertThat(foundExercise).isPresent();
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        Exercise exercise = ExerciseFactory.exercise();
        exerciseRepository.save(exercise);
        em.flush();
        em.clear();

        // when
        exerciseRepository.delete(exercise);
        Exercise foundExercise = (Exercise) em.createNativeQuery(
                "SELECT * FROM exercise WHERE id = :id",
                        Exercise.class
                )
                .setParameter("id", exercise.getId())
                .getSingleResult();

        // then
        LocalDateTime deletedAt = (LocalDateTime) getField(foundExercise, "deletedAt");
        assertThat(deletedAt).isNotNull();
    }
}