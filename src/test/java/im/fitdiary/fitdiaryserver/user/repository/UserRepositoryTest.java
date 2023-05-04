package im.fitdiary.fitdiaryserver.user.repository;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
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
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("soft delete")
    void softDelete() {
        // given
        User user = UserFactory.emailUser();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        userRepository.delete(user);
        User findUser = (User) em.createNativeQuery(
                "select * from user where id = :id",
                        User.class
                )
                .setParameter("id", user.getId())
                .getSingleResult();

        // then
        assertThat(findUser.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("findByLoginId")
    void findByLoginId() {
        // given
        User user = UserFactory.emailUser();
        String wrongLoginId = "wrongId";
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        Optional<User> findUser = userRepository.findByLoginId(user.getAuth().getLoginId());

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getId()).isEqualTo(user.getId());
        assertThat(userRepository.findByLoginId(wrongLoginId)).isNotPresent();
    }

    @Test
    @DisplayName("findAuthByUserId")
    void findAuthByUserId() {
        // given
        User user = UserFactory.emailUser();
        Long wrongId = 1000000L;
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        Optional<User> findUser = userRepository.findAuthByUserId(user.getId());

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getId()).isEqualTo(user.getId());
        assertThat(userRepository.findAuthByUserId(wrongId)).isNotPresent();
    }
}