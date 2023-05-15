package im.fitdiary.fitdiaryserver.auth.data;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.config.P6SpySqlFormatConfig;
import im.fitdiary.fitdiaryserver.config.QuerydslConfig;
import im.fitdiary.fitdiaryserver.util.factory.auth.AuthFactory;
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

@DataJpaTest(showSql = false)
@Import({AuditingConfig.class, QuerydslConfig.class, P6SpySqlFormatConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthUserRepositoryTest {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    EntityManager em;

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("createEmailUser")
        void createEmailUser() {
            // given
            AuthUser authUser = AuthFactory.emailUser();
            authUserRepository.save(authUser);
            em.flush();
            em.clear();

            // when
            Optional<AuthUser> foundAuthUser = authUserRepository.findById(authUser.getId());

            // then
            assertThat(foundAuthUser).isPresent();
            assertThat(foundAuthUser.get().getLoginType()).isEqualTo(UserLoginType.EMAIL);
            assertThat(foundAuthUser.get().getPassword()).isNotNull();
            assertThat(foundAuthUser.get().getRefreshToken()).isNull();
        }

        @Test
        @DisplayName("createKakaoUser")
        void createKakaoUser() {
            // given
            AuthUser authUser = AuthFactory.kakaoUser();
            authUserRepository.save(authUser);
            em.flush();
            em.clear();

            // when
            Optional<AuthUser> foundAuthUser = authUserRepository.findById(authUser.getId());

            // then
            assertThat(foundAuthUser).isPresent();
            assertThat(foundAuthUser.get().getLoginType()).isEqualTo(UserLoginType.KAKAO);
            assertThat(foundAuthUser.get().getPassword()).isNull();
            assertThat(foundAuthUser.get().getRefreshToken()).isNull();
        }
    }
}