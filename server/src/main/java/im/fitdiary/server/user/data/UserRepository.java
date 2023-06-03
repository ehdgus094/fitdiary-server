package im.fitdiary.server.user.data;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@BaseMethodLogging
public interface UserRepository extends JpaRepository<User, Long> {
}