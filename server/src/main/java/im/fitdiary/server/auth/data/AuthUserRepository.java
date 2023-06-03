package im.fitdiary.server.auth.data;

import im.fitdiary.server.auth.data.entity.AuthUser;
import im.fitdiary.server.auth.data.entity.UserLoginType;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@BaseMethodLogging
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByLoginIdAndLoginType(String loginId, UserLoginType loginType);

    Optional<AuthUser> findByLoginIdAndLoginTypeOrUserId(String loginId, UserLoginType loginType, Long userId);

    Optional<AuthUser> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
