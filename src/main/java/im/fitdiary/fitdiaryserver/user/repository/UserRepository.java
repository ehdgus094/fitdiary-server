package im.fitdiary.fitdiaryserver.user.repository;

import im.fitdiary.fitdiaryserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.auth where u.auth.loginId = :loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);
}
