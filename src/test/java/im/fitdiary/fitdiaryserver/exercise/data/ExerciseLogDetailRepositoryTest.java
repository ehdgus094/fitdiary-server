package im.fitdiary.fitdiaryserver.exercise.data;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.P6SpySqlFormatConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.fitdiaryserver.util.factory.exercise.ExerciseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@DataJpaTest(showSql = false)
@Import({AuditingConfig.class, QuerydslConfig.class, P6SpySqlFormatConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExerciseLogDetailRepositoryTest {

    @Autowired
    ExerciseLogDetailRepository exerciseLogDetailRepository;

    @Autowired
    ExerciseLogRepository exerciseLogRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    EntityManager em;

    private Exercise exercise;

    private ExerciseLog exerciseLog;

    @BeforeEach
    void init() {
        exercise = ExerciseFactory.exercise();
        exerciseRepository.save(exercise);
        exerciseLog = ExerciseFactory.exerciseLog();
        exerciseLogRepository.save(exerciseLog);
    }

    @Test
    @DisplayName("bulkInsert")
    void bulkInsert() {
        // given
        int dataCount = 10;

        List<CreateExerciseLogDetail> details = new ArrayList<>();
        for (int i = 0; i < dataCount; i++) {
            CreateExerciseLogDetail detail =
                    ExerciseFactory.createExerciseLogDetail(exercise.getId());
            details.add(detail);
        }

        // when
        exerciseLogDetailRepository.bulkInsert(exerciseLog, details);
        long count = exerciseLogDetailRepository.count();

        // then
        assertThat(count).isEqualTo(dataCount);
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        ExerciseLogDetail detail =
                ExerciseFactory.exerciseLogDetail(exercise, exerciseLog, 1);
        exerciseLogDetailRepository.save(detail);
        em.flush();
        em.clear();

        // when
        exerciseLogDetailRepository.delete(detail);
        ExerciseLogDetail foundExerciseLogDetail = (ExerciseLogDetail) em.createNativeQuery(
                "SELECT * FROM exercise_log_detail WHERE id = :id",
                        ExerciseLogDetail.class
                )
                .setParameter("id", detail.getId())
                .getSingleResult();

        // then
        LocalDateTime deletedAt = (LocalDateTime) getField(foundExerciseLogDetail, "deletedAt");
        assertThat(deletedAt).isNotNull();
    }

    @Test
    @DisplayName("bulkUpdate")
    void bulkUpdate() {
        // given
        int inputCount = 10;

        List<CreateExerciseLogDetail> details = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            CreateExerciseLogDetail detail =
                    ExerciseFactory.createExerciseLogDetail(exercise.getId());
            details.add(detail);
        }

        exerciseLogDetailRepository.bulkInsert(exerciseLog, details);

        List<ExerciseLogDetail> all = exerciseLogDetailRepository.findAll();

        Map<Long, ExerciseLogDetailEditor> editors = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            ExerciseLogDetailEditor editor = ExerciseFactory.exerciseLogDetailEditor();
            setField(editor, "count", JsonNullable.of(100));
            editors.put(all.get(i).getId(), editor);
        }

        // when
        exerciseLogDetailRepository.bulkUpdate(exerciseLog, editors);
        Optional<ExerciseLogDetail> detail1 = exerciseLogDetailRepository.findById(all.get(0).getId());
        Optional<ExerciseLogDetail> detail2 = exerciseLogDetailRepository.findById(all.get(1).getId());

        // then
        assertThat(detail1).isPresent();
        assertThat(detail1.get().getCount()).isEqualTo(100);
        assertThat(detail2).isPresent();
        assertThat(detail2.get().getCount()).isEqualTo(100);
    }
}