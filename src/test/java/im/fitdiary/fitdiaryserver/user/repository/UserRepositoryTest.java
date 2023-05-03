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
import java.util.List;

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
    @DisplayName("기본 crud")
    void crud() {
        User user = UserFactory.emailUser();

        // 저장
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getBirthYmd()).isEqualTo(user.getBirthYmd());
        assertThat(savedUser.getGender()).isEqualTo(user.getGender());

        // 조회
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(findUser).isEqualTo(user);

        // 전체 조회
        List<User> all = userRepository.findAll();
        assertThat(all).hasSize(1);

        // 카운트
        long count = userRepository.count();
        assertThat(count).isEqualTo(1);

        // 삭제
        userRepository.delete(user);
        long deleteCount = userRepository.count();
        assertThat(deleteCount).isZero();
    }

    @Test
    @DisplayName("soft delete")
    void softDelete() {
        // given
        User user = UserFactory.emailUser();
        userRepository.save(user);

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
        final String wrongId = "wrongId";
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        User findUser = userRepository
                .findByLoginId(user.getAuth().getLoginId())
                .orElseThrow();

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(userRepository.findByLoginId(wrongId)).isNotPresent();
    }
}