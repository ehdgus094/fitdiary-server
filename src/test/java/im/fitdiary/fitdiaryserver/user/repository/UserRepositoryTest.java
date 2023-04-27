package im.fitdiary.fitdiaryserver.user.repository;

import im.fitdiary.fitdiaryserver.config.AuditingConfig;
import im.fitdiary.fitdiaryserver.exception.InvalidLoginInfoException;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("UserRepository")
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
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getBirthYmd(), savedUser.getBirthYmd());
        assertEquals(user.getGender(), savedUser.getGender());
        assertEquals(user.getHeight(), savedUser.getHeight());
        assertEquals(user.getWeight(), savedUser.getWeight());

        // 조회
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user, findUser);

        // 전체 조회
        List<User> all = userRepository.findAll();
        assertEquals(1, all.size());

        // 카운트
        long count = userRepository.count();
        assertEquals(1, count);

        // 삭제
        userRepository.delete(user);
        long deleteCount = userRepository.count();
        assertEquals(0, deleteCount);
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
        assertNotNull(findUser.getDeletedAt());
    }

    @Test
    @DisplayName("findByLoginId")
    void findByLoginId() {
        // given
        User user = UserFactory.emailUser();
        userRepository.save(user);
        em.flush();
        em.clear();

        // when
        User findUser = userRepository
                .findByLoginId(user.getAuth().getLoginId())
                .orElseThrow();

        // then
        assertEquals(user.getId(), findUser.getId());
        assertThrows(InvalidLoginInfoException.class, () -> {
            userRepository
                    .findByLoginId("wrongId")
                    .orElseThrow(InvalidLoginInfoException::new);
        });
    }
}