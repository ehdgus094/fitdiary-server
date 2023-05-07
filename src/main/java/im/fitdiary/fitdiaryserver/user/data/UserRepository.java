package im.fitdiary.fitdiaryserver.user.data;

import im.fitdiary.fitdiaryserver.user.data.entity.LoginType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.auth where u.auth.loginId = :loginId and u.auth.loginType = :loginType")
    Optional<User> findByLoginIdAndLoginType(@Param("loginId") String loginId, @Param("loginType") LoginType loginType);

    @Query("select u from User u join fetch u.auth where u.id = :id")
    Optional<User> findAuthByUserId(@Param("id") Long id);
}
