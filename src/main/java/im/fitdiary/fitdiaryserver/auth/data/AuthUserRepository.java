package im.fitdiary.fitdiaryserver.auth.data;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.data.entity.UserLoginType;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@BaseMethodLogging
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByLoginIdAndLoginType(String loginId, UserLoginType loginType);

    Optional<AuthUser> findByLoginIdAndLoginTypeOrUserId(String loginId, UserLoginType loginType, Long userId);

    Optional<AuthUser> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
