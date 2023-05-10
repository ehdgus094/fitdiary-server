package im.fitdiary.fitdiaryserver.bodylog.data;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.util.factory.bodylog.BodyLogFactory;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BodyLogRepositoryTest {

    @Autowired
    BodyLogRepository bodyLogRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    private User user;

    @BeforeEach
    void init() {
        user = UserFactory.emailUser();
        userRepository.save(user);
    }

    @Test
    @DisplayName("create")
    void create() {
        // given
        BodyLog bodyLog = BodyLogFactory.bodyLog(user);
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
        BodyLog bodyLog = BodyLogFactory.bodyLog(user);
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
        assertThat(foundBodyLog.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("findLatestOne")
    void findLatestOne() {
        // given
        BodyLog bodyLog1 = BodyLogFactory.bodyLog(user);
        bodyLogRepository.save(bodyLog1);
        BodyLog bodyLog2 = BodyLogFactory.bodyLog(user);
        bodyLogRepository.save(bodyLog2);
        BodyLog bodyLog3 = BodyLogFactory.bodyLog(user);
        bodyLogRepository.save(bodyLog3);

        // when
        Optional<BodyLog> foundBodyLog = bodyLogRepository.findLatestOne(user.getId());

        // then
        assertThat(foundBodyLog).isPresent();
        assertThat(foundBodyLog.get().getCreatedAt()).isEqualTo(bodyLog3.getCreatedAt());
    }
}