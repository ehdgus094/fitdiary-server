package im.fitdiary.fitdiaryserver.user.data;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    @DisplayName("createEmailUser")
    void createEmailUser() {
        // given
        User user = UserFactory.emailUser();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getAuth().getLoginType()).isEqualTo(LoginType.EMAIL);
        assertThat(foundUser.get().getAuth().getLoginId())
                .isEqualTo(foundUser.get().getEmail());
        assertThat(foundUser.get().getAuth().getPassword()).isNotNull();
        assertThat(foundUser.get().getAuth().getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("createKakaoUser")
    void createKakaoUser() {
        // given
        User user = UserFactory.kakaoUser();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getAuth().getLoginType()).isEqualTo(LoginType.KAKAO);
        assertThat(foundUser.get().getAuth().getPassword()).isNull();
        assertThat(foundUser.get().getAuth().getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("softDelete")
    void softDelete() {
        // given
        User user = UserFactory.emailUser();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        userRepository.delete(user);
        User foundUser = (User) em.createNativeQuery(
                "select * from user where id = :id",
                        User.class
                )
                .setParameter("id", user.getId())
                .getSingleResult();

        // then
        assertThat(foundUser.getDeletedAt()).isNotNull();
    }

    @Nested
    @DisplayName("findByLoginIdAndLoginType")
    class FindByLoginIdAndLoginType {

        private User user;

        @BeforeEach
        void create() {
            user = UserFactory.emailUser();
            userRepository.save(user);
        }

        @Test
        @DisplayName("found")
        void found() {
            // when
            Optional<User> foundUser =
                    userRepository.findByLoginIdAndLoginType(
                            user.getAuth().getLoginId(),
                            user.getAuth().getLoginType()
                    );

            // then
            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().getId()).isEqualTo(user.getId());
        }

        @Test
        @DisplayName("notFound_wrongLoginId")
        void notFound_wrongLoginId() {
            // given
            String wrongLoginId = "wrongId";

            // when
            Optional<User> foundUser =
                    userRepository.findByLoginIdAndLoginType(
                            wrongLoginId,
                            user.getAuth().getLoginType()
                    );

            // then
            assertThat(foundUser).isNotPresent();
        }

        @Test
        @DisplayName("notFound_wrongLoginType")
        void notFound_wrongLoginType() {
            // given
            LoginType wrongLoginType = LoginType.KAKAO;

            // when
            Optional<User> foundUser =
                    userRepository.findByLoginIdAndLoginType(
                            user.getAuth().getLoginId(),
                            wrongLoginType
                    );

            // then
            assertThat(foundUser).isNotPresent();
        }
    }

    @Nested
    @DisplayName("findAuthByUserId")
    class FindAuthByUserId {

        private User user;

        @BeforeEach
        void create() {
            user = UserFactory.emailUser();
            userRepository.save(user);
        }

        @Test
        @DisplayName("found")
        void found() {
            // when
            Optional<User> foundUser = userRepository.findAuthByUserId(user.getId());

            // then
            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().getId()).isEqualTo(user.getId());
        }

        @Test
        @DisplayName("notFound")
        void notFound() {
            // given
            Long wrongId = 1000000L;

            // when
            Optional<User> foundUser = userRepository.findAuthByUserId(wrongId);

            // then
            assertThat(foundUser).isNotPresent();
        }
    }
}