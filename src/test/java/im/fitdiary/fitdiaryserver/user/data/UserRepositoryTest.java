package im.fitdiary.fitdiaryserver.user.data;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.P6SpySqlFormatConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
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
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("create")
    void create() {
        // given
        User user = UserFactory.user();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then
        assertThat(foundUser).isPresent();
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        User user = UserFactory.user();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        userRepository.delete(user);
        User foundUser = (User) em.createNativeQuery(
                "SELECT * FROM user WHERE id = :id",
                        User.class
                )
                .setParameter("id", user.getId())
                .getSingleResult();

        // then
        LocalDateTime deletedAt = (LocalDateTime) getField(foundUser, "deletedAt");
        assertThat(deletedAt).isNotNull();
    }
}