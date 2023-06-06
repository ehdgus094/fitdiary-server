package im.fitdiary.server.body.data;

import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.config.AuditingConfig;
import im.fitdiary.server.config.P6SpySqlFormatConfig;
import im.fitdiary.server.config.QuerydslConfig;
import im.fitdiary.server.util.factory.body.BodyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@DataJpaTest(showSql = false)
@Import({AuditingConfig.class, QuerydslConfig.class, P6SpySqlFormatConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BodyLogRepositoryTest {

    @Autowired
    BodyLogRepository bodyLogRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("create")
    void create() {
        // given
        BodyLog bodyLog = BodyFactory.bodyLog();
        bodyLogRepository.save(bodyLog);
        em.flush();
        em.clear();

        // when
        Optional<BodyLog> foundBodyLog = bodyLogRepository.findById(bodyLog.getId());

        // then
        assertThat(foundBodyLog).isPresent();
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        BodyLog bodyLog = BodyFactory.bodyLog();
        bodyLogRepository.save(bodyLog);
        em.flush();
        em.clear();

        // when
        bodyLogRepository.delete(bodyLog);
        BodyLog foundBodyLog = (BodyLog) em.createNativeQuery(
                "SELECT * FROM body_log WHERE id = :id",
                        BodyLog.class
                )
                .setParameter("id", bodyLog.getId())
                .getSingleResult();

        // then
        LocalDateTime deletedAt = (LocalDateTime) getField(foundBodyLog, "deletedAt");
        assertThat(deletedAt).isNotNull();
    }

    @Test
    @DisplayName("findLatestOne")
    void findLatestOne() {
        // given
        LocalDateTime now = LocalDateTime.now();
        BodyLog bodyLog1 = BodyFactory.bodyLog();
        setField(bodyLog1, "measuredAt", now.minusDays(1));
        bodyLogRepository.save(bodyLog1);
        BodyLog bodyLog2 = BodyFactory.bodyLog();
        setField(bodyLog2, "measuredAt", now.minusDays(2));
        bodyLogRepository.save(bodyLog2);
        BodyLog bodyLog3 = BodyFactory.bodyLog();
        setField(bodyLog3, "measuredAt", now.minusDays(3));
        bodyLogRepository.save(bodyLog3);

        // when
        Optional<BodyLog> foundBodyLog = bodyLogRepository.findLatestOne(bodyLog1.getUserId());

        // then
        assertThat(foundBodyLog).isPresent();
        assertThat(foundBodyLog.get().getMeasuredAt()).isEqualTo(bodyLog1.getMeasuredAt());
    }

    @Test
    @DisplayName("find")
    void find() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<BodyLog> bodyLogs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BodyLog bodyLog = BodyFactory.bodyLog();
            setField(bodyLog, "measuredAt", now.minusDays(i));
            bodyLogs.add(bodyLog);
        }
        bodyLogRepository.saveAll(bodyLogs);

        // when
        Slice<BodyLog> page_1 = bodyLogRepository
                .find(PageRequest.of(0, 19), bodyLogs.get(0).getUserId());
        Slice<BodyLog> page_2 = bodyLogRepository
                .find(PageRequest.of(1, 19), bodyLogs.get(0).getUserId());

        // then
        assertThat(page_1.getContent()).hasSize(19);
        assertThat(page_1.hasNext()).isTrue();
        assertThat(page_2.getContent()).hasSize(1);
        assertThat(page_2.hasNext()).isFalse();
    }
}